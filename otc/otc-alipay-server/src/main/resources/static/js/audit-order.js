var auditOrderVM = new Vue({
	el : '#auditOrder',
	data : {
		appealTypeDictItems: [],
		showWaitConfirmOrderFlag: true,
		waitConfirmOrders: [],
		bankCardList: [],
		selectedOrder: {},
		appealType: '',
		orderStatus: '1',
		bankCard: '',
		actualPayAmount: '',
		pageNum: 1,
		receiveOrderTime: dayjs().format('YYYY-MM-DD'),
		totalPage: 1,
		userSreenshotIds: ''
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
		headerVM.title = '审核订单';
		//that.loadAppealTypeDictItem();
		that.getBankCard();
		that.loadPlatformOrder();
		$('.sreenshot').on('filebatchuploadsuccess', function (event, data) {
			that.userSreenshotIds = data.response.result.join(',');
			that.userStartAppealInner();
		});
	},
	methods : {
		query : function() {
			this.pageNum = 1;
			this.loadPlatformOrder();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadPlatformOrder();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadPlatformOrder();
		},
		/**
		 * 加载申诉类型字典项
		
		loadAppealTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealType'
				}
			}).then(function(res) {
				this.appealTypeDictItems = res.body.data;
			});
		},
		 */
		enterOrder: function (orderId, bank) {
			debugger;
			var that = this;
			var bankCard = $("." + orderId + "").val();
			if (bank == '' || bank == null) {
				if (bankCard == null || bankCard == '') {
					layer.alert('请确认出款卡', {
						title: '提示',
						icon: 7,
						time: 3000
					});
				} else {
					that.$http.get('/qrcode/setBankCard', {
						params: {
							bankCard: bankCard,
							orderId: orderId
						}
					}).then(function (res) {
						window.location.href = res.body.result;

					});
				}
			} else {
				that.$http.get('/qrcode/setBankCard', {
					params: {
						bankCard: bankCard,
						orderId: orderId
					}
				}).then(function (res) {
					window.location.href = res.body.result;

				});
			}
		},

		getBankCard: function () {
			var that = this;
			that.$http.get('/qrcode/getBankCardList').then(function (res) {
				that.bankCardList = res.body.result;
			});

		},
		loadPlatformOrder: function () {
			var that = this;
			that.$http.get('/order/findMyWaitConfirmOrder', {
				params: {
					pageSize: 5,
					orderStatus: this.orderStatus,
					pageNum: that.pageNum,
					createTime: that.receiveOrderTime
				}
			}).then(function (res) {
				that.waitConfirmOrders = res.body.result.content;
				that.pageNum = res.body.result.pageNum;
				that.totalPage = res.body.result.totalPage;
			});
		},
		confirmToPaid : function(orderId) {
			var that = this;
			layer.msg('确认收到款项？', {
				  	time: 0 //不自动关闭
				  ,btn: ['已收到', '点错了']
				  ,yes: function(index){
					  that.$http.get('/order/userConfirmToPaid', {
							params : {
								orderId : orderId
							}
						}).then(function(res) {
							if(res.body.success){
								layer.alert('操作成功', {
									icon : 1,
									time : 2000,
									shade : false
								});
								that.loadPlatformOrder();
							}else{
								layer.alert(res.body.message, {
									icon : 1,
									time : 2000,
									shade : false
								});
								that.loadPlatformOrder();
							}
						});
					  return ;
				  }
					,no:function(index){
						return ;
					}
				});
			 
		},

		showAppealPage : function(order) {
			this.selectedOrder = order;
			this.appealType = '';
			this.actualPayAmount = '';
			this.userSreenshotIds = '';
			headerVM.title = '申诉';
			this.showWaitConfirmOrderFlag = false;
			this.initFileUploadWidget();
		},

		showWaitConfirmOrderPage : function() {
			headerVM.title = '审核订单';
			this.showWaitConfirmOrderFlag = true;
			this.loadPlatformOrder();
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
			$('.sreenshot').fileinput('destroy').fileinput({
				uploadAsync : false,
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
				maxFileCount : 2,
				uploadUrl : '/storage/uploadPic',//上传图片接口
				enctype : 'multipart/form-data',
				allowedFileExtensions : [ 'jpg', 'png', 'bmp', 'jpeg' ],
				initialPreview : initialPreview,
				initialPreviewAsData : true,
				initialPreviewConfig : initialPreviewConfig
			});
		},

		userStartAppeal : function() {
			var that = this;
			if (that.appealType == null || that.appealType == '') {
				layer.alert('请选择申诉类型', {
					title : '提示',
					icon : 7,
					time : 3000
				});
				return;
			}
			if (that.appealType == '2') {
				if (that.actualPayAmount == null || that.actualPayAmount == '') {
					layer.alert('请输入实际支付金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				if (that.selectedOrder.gatheringAmount < that.actualPayAmount) {
					layer.alert('实际支付金额须小于收款金额', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				var filesCount = $('.sreenshot').fileinput('getFilesCount');
				if (filesCount == 0) {
					layer.alert('请上传截图', {
						title : '提示',
						icon : 7,
						time : 3000
					});
					return;
				}
				$('.sreenshot').fileinput('upload');
			} else {
				that.userSreenshotIds = '';
				that.userStartAppealInner();
			}
		},

		userStartAppealInner : function() {
			var that = this;
			that.$http.post('/order/userStartAppeal', {
				appealType : that.appealType,
				actualPayAmount : that.actualPayAmount,
				userSreenshotIds : that.userSreenshotIds,
				merchantOrderId : that.selectedOrder.orderId,
			}, {
				emulateJSON : true
			}).then(function(res) {
				layer.alert('发起申诉成功', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.showWaitConfirmOrderPage();
			});
		}
	}
});