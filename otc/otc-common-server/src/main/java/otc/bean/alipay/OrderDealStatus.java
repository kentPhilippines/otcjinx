package otc.bean.alipay;

public enum OrderDealStatus {
    成功("成功", 2), 未收到回调("未收到回调", 3),
    处理中("处理中", 1), 失败("失败", 4),
    人工处理("人工处理", 7);
    private String name;
    private Integer index;

    private OrderDealStatus(String name, Integer index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }
}
