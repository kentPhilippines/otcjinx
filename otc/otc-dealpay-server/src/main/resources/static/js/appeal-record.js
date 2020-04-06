var appealRecordVM = new Vue({
	el : '#appeal-record',
	data : {
		gatheringChannelCode : '',
		gatheringChannelDictItems : [],
		orderNo : '',
		appealType : '',
		appealTypeDictItems : [],
		appealState : '',
		appealStateDictItems : [],
		initiationTime : dayjs().format('YYYY-MM-DD'),
		appealRecords : [],
		pageNum : 1,
		totalPage : 1,
		remitOrderState : '',
		receiveOrderState : '',
		accountName:'',
		cardFee : '',
		fee : '',
		accountName :''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '接单设置';
		headerVM.showBackFlag = true;
		var that = this;
		that.getUserAccountInfo();
	/*	that.loadGatheringChannelDictItem();
		that.loadAppealTypeDictItem();
		that.loadAppealStateDictItem();
		that.loadByPage();*/
	},
	methods : {
		 
		/*loadAppealTypeDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealType'
				}
			}).then(function(res) {
				this.appealTypeDictItems = res.body.data;
			});
		},

		loadAppealStateDictItem : function() {
			var that = this;
			that.$http.get('/dictconfig/findDictItemInCache', {
				params : {
					dictTypeCode : 'appealState'
				}
			}).then(function(res) {
				this.appealStateDictItems = res.body.data;
			});
		},

		query : function() {
			this.pageNum = 1;
			this.loadByPage();
		},

		prePage : function() {
			this.pageNum = this.pageNum - 1;
			this.loadByPage();
		},

		nextPage : function() {
			this.pageNum = this.pageNum + 1;
			this.loadByPage();
		},

		loadByPage : function() {
			var that = this;
			that.$http.get('/appeal/findUserAppealRecordByPage', {
				params : {
					pageSize : 5,
					pageNum : that.pageNum,
					gatheringChannelCode : that.gatheringChannelCode,
					orderNo : that.orderNo,
					appealType : that.appealType,
					appealState : that.appealState,
					initiationStartTime : that.initiationTime,
					initiationEndTime : that.initiationTime
				}
			}).then(function(res) {
				that.appealRecords = res.body.data.content;
				that.pageNum = res.body.data.pageNum;
				that.totalPage = res.body.data.totalPage;
			});
		},

		userCancelAppeal : function(appealId) {
			var that = this;
			that.$http.get('/appeal/userCancelAppeal', {
				params : {
					appealId : appealId
				}
			}).then(function(res) {
				layer.alert('撤销成功', {
					icon : 1,
					time : 2000,
					shade : false
				});
				that.showViewDetailsPage(appealId);
			});
		},

		showViewDetailsPage : function(id) {
			window.location.href = '/appeal-details?id=' + id;
		}*/
		getUserAccountInfo : function() {
			var that = this;
			that.$http
					.get('/userAccount/getUserAccountInfo')
					.then(
							function(res) {
								if (res.body.result != null) {
									that.accountName = res.body.result.userName;
									that.remitOrderState = res.body.result.remitOrderState;
									that.receiveOrderState = res.body.result.receiveOrderState;
									that.cardFee = res.body.result.cardFee;
									that.fee = res.body.result.fee;
								}
							});
		},
		updataReceiveOrderStateNO : function(){//入款开启
			 var that = this;
				that.$http.get('/userAccount/updataReceiveOrderStateNO',{
				}).then(function(res) {
					if(res.body.success){
						that.getUserAccountInfo()
					}else{
						layer.msg(res.body.message)
					}
				});
		 },
		 updataReceiveOrderStateOFF : function (){//取款关闭
			var that = this;
			that.$http.get('/userAccount/updataReceiveOrderStateOFF',{
			}).then(function(res) {
				if(res.body.success){
					that.getUserAccountInfo()
				}else{
					layer.msg(res.body.message)
				}
			});
		},
		updataRemitOrderStateNO : function(){//出款开启
			var that = this;
			that.$http.get('/userAccount/updataRemitOrderStateNO',{
			}).then(function(res) {
				if(res.body.success){
					that.getUserAccountInfo()
				}else{
					layer.msg(res.body.message)
				}
			});
			
		},
		updataRemitOrderStateOFF : function(){//出款关闭
			var that = this;
			that.$http.get('/userAccount/updataRemitOrderStateOFF',{
			}).then(function(res) {
				if(res.body.success){
					that.getUserAccountInfo()
				}else{
					layer.msg(res.body.message)
				}
			});
		},
	}
});