var gatheringCodeVM = new Vue({
	el : '#gatheringCode',
	data : {
		gatheringChannelCode  : '',
		gatheringChannelDictItems : [],
		status : '',
		gatheringCodeStateDictItems : [],
		accountTime : dayjs().format('YYYY-MM-DD'),
		gatheringCodes : [],
		pageNum : 1,
		totalPage : 1,
		showGatheringCodeFlag : true,

		editGatheringCode : {
			bankCode : '',//银行卡属性
			status : '',//银行卡状态
			account : '',//开户人
			bank : '',//开户行
			phone:'',//开户手机号
			bankNo:'',//开户银行卡号,
			qrcodeNote : ''
		},
		showEditGatheringCodeFlag : false,
	},
	 filters: {
		 dateFilter: function (data, format = "") {
             var dt = new Date(data);
             var y = dt.getFullYear();
             var m = (dt.getMonth()+1).toString().padStart(2,"0");
             var d = dt.getDate().toString().padStart(2,"0");
             var h = dt.getHours().toString().padStart(2,"0");
             var mm = dt.getMinutes().toString().padStart(2,"0");
             var s = dt.getSeconds().toString().padStart(2,"0");
             if (format.toLocaleLowerCase() === "yyyy-mm-dd" ||
                 format.toLocaleLowerCase() === "") {
                 return `${y}-${m}-${d}`;
             } else if (format.toLocaleLowerCase() === "yyyy/mm/dd") {
                 return `${y}/${m}/${d}`;
             } else if (format.toLocaleLowerCase() === "yyyy-mm-dd hh:mm:ss") {
                 return `${y}-${m}-${d} ${h}:${mm}:${s}`;
             } else if (format.toLocaleLowerCase() === "yyyy/mm/dd hh:mm:ss") {
                 return `${y}/${m}/${d} ${h}:${mm}:${s}`;
             } else {
                 return `输入的时间格式有误`;
             }
         }
	 },
	computed : {},
	created : function() {
	},
	mounted : function() {
		var that = this;
		headerVM.title = '银行卡';
		headerVM.showBackFlag = true;
		that.loadGatheringChannelDictItem();
		that.loadGatheringCodeByPage();

		$('.gathering-code-pic').on('fileuploaded', function(event, data, previewId, index) {
			that.editGatheringCode.qrcodeId = data.response.result.join(',');
			that.addOrUpdateGatheringCodeInner();
		});
	},
	methods : {
		/**
		 * 获取收款渠道
		 */
		loadGatheringChannelDictItem : function() {
			var that = this;
			var result = [
				 {payCode : 'R' , payName : '收款卡'}
				,{payCode : 'W' , payName : '出款卡'}
			]
		//	that.$http.get('/recharge/findEnabledPayType' ).then(function(res) {
				this.gatheringChannelDictItems = result;
		//	});
		},

		/*
		 * loadGatheringCodeStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'gatheringCodeState'
				}
			}).then(function(res) {
				this.gatheringCodeStateDictItems = res.body.data;
			});
		},
		 */
		

		query : function() {
			this.pageNum = 1;
			this.loadGatheringCodeByPage();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadGatheringCodeByPage();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadGatheringCodeByPage();
		},

		loadGatheringCodeByPage : function() {
			var that = this;
			that.$http.get('/statisticalAnalysis/findMyGatheringCodeByPage', {
				params : {
					pageSize : 5,
					pageNum : that.pageNum,
					status : that.status,
					bankCode : that.bankCode
				}
			}).then(function(res) {
				that.gatheringCodes = res.body.result.content;
				that.pageNum = res.body.result.pageNum;
				that.totalPage = res.body.result.totalPage;
			});
		},

		initFileUploadWidget : function(storageId) {
			var initialPreview = [];
			var initialPreviewConfig = [];
			if (storageId != null) {
				initialPreview.push('/storage/fetch/' + storageId);
				initialPreviewConfig.push({
					downloadUrl : '/storage/fetch/' + storageId
				});
			}
			$('.gathering-code-pic').fileinput('destroy').fileinput({
				browseOnZoneClick : true,
				showBrowse : false,
				showCaption : false,
				showClose : true,
				showRemove : false,
				showUpload : false,
				dropZoneTitle : '点击选择图片',
				dropZoneClickTitle : '',
				layoutTemplates : {
					footer : ''
				},
				maxFileCount : 1,
				uploadUrl : '/storage/uploadPic',//上传图片接口
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		switchGatheringAmountMode : function() {
			if (!this.editGatheringCode.fixedGatheringAmount) {
				this.editGatheringCode.gatheringAmount = '';
			}
		},

		/**
		 * <p>编辑二维码</p>
		 */
		showEditGatheringCodePage : function(gatheringCodeId) {
			var that = this;
			if (gatheringCodeId == null || gatheringCodeId == '') {
				that.editGatheringCode = {
						bankCode : '',
					status : '',
					fixedGatheringAmount : true,
					gatheringAmount : '',
					account : ''
				};
				that.showEditGatheringCodePageInner();
				that.initFileUploadWidget();
			} else {
				that.$http.get('/statisticalAnalysis/findMyGatheringCodeById', {
					params : {
						id : gatheringCodeId,
					}
				}).then(function(res) {
					that.editGatheringCode.bankCode  =  res.body.result.bankCode;//银行卡属性
					that.editGatheringCode.status  =  res.body.result.status;//银行卡状态
					that.editGatheringCode.account  =  res.body.result.accountHolder;//开户人
					that.editGatheringCode.bank   =  res.body.result.openAccountBank;//开户行
					that.editGatheringCode.phone  =  res.body.result.phone;//开户手机号
					that.editGatheringCode.bankNo  =  res.body.result.bankCardAccount;//开户银行卡号,
					that.editGatheringCode.qrcodeNote   =  res.body.result.qrcodeNote;
					that.showEditGatheringCodePageInner();
					that.initFileUploadWidget(res.body.result.qrcodeId);
				});
			}
		},

		showEditGatheringCodePageInner : function() {
			headerVM.showBackFlag = false;
			headerVM.title = '编辑银行卡';
			this.showGatheringCodeFlag = false;
			this.showEditGatheringCodeFlag = true;
		},

		hideEditGatheringCodePage : function() {
			headerVM.showBackFlag = true;
			headerVM.title = '银行卡';
			this.showGatheringCodeFlag = true;
			this.showEditGatheringCodeFlag = false;
		},

		addOrUpdateGatheringCode : function() {
			var that = this;
			var editGatheringCode = that.editGatheringCode;
			if (editGatheringCode.bankCode == null || editGatheringCode.bankCode == '') {
				layer.alert('请选择银行卡属性', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.status == null || editGatheringCode.status === '') {
				layer.alert('请选择状态', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.account == null || editGatheringCode.account == '') {
				layer.alert('请填写开户人', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.phone == null || editGatheringCode.phone == '') {
				layer.alert('请填写银行卡绑定手机号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.qrcodeNote == null || editGatheringCode.qrcodeNote == '') {
				layer.alert('请填写开户行地址', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (editGatheringCode.bankNo == null || editGatheringCode.bankNo == '') {
				layer.alert('请填写银行卡账号', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			
			that.addOrUpdateGatheringCodeInner();
		},

		
		addOrUpdateGatheringCodeInner : function() {
			var that = this;
			var bank = {
					bankcardAccount :  that.editGatheringCode.bankNo,
					accountHolder : that.editGatheringCode.account,
					openAccountBank : that.editGatheringCode.bank,
					bankcode :  that.editGatheringCode.bankCode,
					phone : that.editGatheringCode.phone
			}
			that.$http.post('/userAccount/bindBankInfo',  bank ).then(function(res) {
				if(res.body.success){
					layer.alert('操作成功!', {
						icon : 1,
						time : 3000,
						shade : false
					});
				}
				that.hideEditGatheringCodePage();
				that.query();
			});
		},

		delGatheringCode : function(gatheringCodeId) {
			var that = this;
			that.$http.get('/statisticalAnalysis/delMyGatheringCodeById', {
				params : {
					id : gatheringCodeId,
				}
			}).then(function(res) {
				layer.alert('操作成功!', {
					icon : 1,
					time : 3000,
					shade : false
				});
				that.hideEditGatheringCodePage();
				that.query();
			});
		}
	}
});