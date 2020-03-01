package alipay.manage.bean.util;

public class AreaIp {
	 	private String ip;//IP数据
	    private String country;//国家
	    private String area;//区域
	    private String region;//省份
	    private String city;//城市
	    private String county;//郡县
	    private String isp;//运营商
	    private String countryId;//国家id    如中国  CN
	    private String areaId;//区域区域id
	    private String regionId;//省份id
	    private String cityId;//城市id
	    private String countyId;//郡县id
	    private String ispId;//运营商id
	    private String ipType;//IP类型			1 下游商户 2上游渠道 3 四方平台
	    private String ipFreeze;//是否为黑名单		1 冻结  2 可用
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getArea() {
			return area;
		}
		public void setArea(String area) {
			this.area = area;
		}
		public String getRegion() {
			return region;
		}
		public void setRegion(String region) {
			this.region = region;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getCounty() {
			return county;
		}
		public void setCounty(String county) {
			this.county = county;
		}
		public String getIsp() {
			return isp;
		}
		public void setIsp(String isp) {
			this.isp = isp;
		}
		public String getCountryId() {
			return countryId;
		}
		public void setCountryId(String countryId) {
			this.countryId = countryId;
		}
		public String getAreaId() {
			return areaId;
		}
		public void setAreaId(String areaId) {
			this.areaId = areaId;
		}
		public String getRegionId() {
			return regionId;
		}
		public void setRegionId(String regionId) {
			this.regionId = regionId;
		}
		public String getCityId() {
			return cityId;
		}
		public void setCityId(String cityId) {
			this.cityId = cityId;
		}
		public String getCountyId() {
			return countyId;
		}
		public void setCountyId(String countyId) {
			this.countyId = countyId;
		}
		public String getIspId() {
			return ispId;
		}
		public void setIspId(String ispId) {
			this.ispId = ispId;
		}
		public String getIpType() {
			return ipType;
		}
		public void setIpType(String ipType) {
			this.ipType = ipType;
		}
		public String getIpFreeze() {
			return ipFreeze;
		}
		public void setIpFreeze(String ipFreeze) {
			this.ipFreeze = ipFreeze;
		}
}
