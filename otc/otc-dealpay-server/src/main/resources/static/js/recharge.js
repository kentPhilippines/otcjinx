var rechargeVM = new Vue({
	el : '#recharge',
	data : {
		quickInputAmount : '',
		rechargeExplain : '',
		payChannel : '',
		payType : '',
		mobile : '',
		amountList : [],
		payTypes : [],
		selectedPayType : {},
		payChannels : [],
		selectedPayChannel : '',
		depositDate : dayjs().format('YYYY-MM-DD'),
		depositTime : dayjs().format('HH:mm'),
		depositor : '',
		amount : ''
	},
	computed : {},
	created : function() {
	},
	mounted : function() {
		headerVM.title = '充值';
		headerVM.showBackFlag = true;
		var clipboard = new ClipboardJS('.copy-btn');
		clipboard.on('success', function(e) {
			layer.alert('复制成功!', {
				icon : 1,
				time : 3000,
				shade : false
			});
		});
		this.loadRechargeSetting();
		this.loadPayType();
		this.loadAmountList();
		this.clickAmount();
	},
	methods : {
		loadRechargeSetting : function() {
			var that = this;
		},
		clickAmount: function(amount){
			var that = this;
			that.amount = amount;
		},
		loadAmountList : function(){
			var that = this;
			that.amountList = [
				 {amount : '5000元' ,code :'5000'}
				,{amount : '10000元',code :'10000'}
				,{amount : '20000元',code :'20000'}
				,{amount : '25000元',code :'25000'}
				,{amount :'30000元',code :'30000'}
				,{amount : '35000元',code :'35000'}
				,{amount : '40000元',code :'40000'}
				,{amount : '45000元',code :'45000'}
				,{amount : '50000元',code :'50000'}
			]
		},
		loadPayType : function() {
			var that = this;
			that.$http.get('/recharge/findEnabledPayType').then(function(res) {
				that.payTypes = res.body.result;
				that.payChannels = that.payTypes;
				if (that.payTypes.length > 0) {
					that.selectedPayType = that.payTypes[0];
				} else {
					layer.alert('暂没有可用的充值通道', {
						title : '提示',
						icon : 7,
						time : 3000
					});
				}
			});
		},
		confirmSubmit : function() {
			var that = this;
			if (that.selectedPayType.bankCardFlag == 1) {
				if (that.depositor == null || that.depositor == '') {
					layer.alert('请输入存款人姓名');
					return;
				}
			}
			if (that.amount == null || that.amount == '') {
				layer.alert('请输入充值金额');
				return;
			}
			if (that.mobile == null || that.mobile == '') {
				layer.alert('请输入充值手机号');
				return;
			}
			layer.open({
				title : '提示',
				icon : 7,
				closeBtn : 0,
				btn : [],
				content : '正在创建充值订单...',
				time : 2000
			});
			that.$http.post('/recharge/generateRechargeOrder', {
				qrRechargeType : that.payType,
				depositor : that.depositor,
				amount : that.amount,
				phone : that.mobile
			}, {
				emulateJSON : true
			}).then(function(res) {
					if(res){
						if(res.body.success){
							layer.open({
								title : '提示',
								icon : '1',
								closeBtn : 0,
								btn : [],
								content : '充值订单创建成功,正在跳转到支付页面!',
								time : 2000,
								end : function() {
										window.location.href = res.body.result;
								}
							});
						} else {//失败的时候
							　 layer.open({
									title : '提示',
									icon : '1',
									closeBtn : 0,
									btn : [],
									content : '充值订单创建失败，正在跳转主页',
									time : 2000,
									end : function() {
											window.location.href = "/my-home-page";
									}
								});
						}
					}
			});
		}
	}
});