package test.number;

import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.http.HttpUtil;

public class IP {
    public static void main(String[] args) {


        //  0x0418F374F25EdAb13D38a7D82b445cE9934Bfc12

        //  String s = HttpUtil.get(" https://api.etherscan.io/api?module=account&action=tokentx&address=0x28250971cF8bB17eDB2fD31e72C7fD352ae0eFCB&page=1&offset=5&sort=asc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M");
        String s = HttpUtil.get("https://api.etherscan.io/api?module=account&action=balance&address=0x28250971cF8bB17eDB2fD31e72C7fD352ae0eFCB&tag=latest&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M" +
                "  ");

        //   57784170420634520
        //  8800000000000000


        System.out.println(s);
//https://api.etherscan.io/api?module=account&action=balance&address=0x28250971cF8bB17eDB2fD31e72C7fD352ae0eFCB&tag=latest&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M
        /**
         * {"status":"1","message":"OK","result":[
         * {"blockNumber":"11555015","timeStamp":"1609326501","hash":"0xc8e380198c3c19ee40f6f3d068821ce34afabbe32a81bf4f0f4e0e7616e864a1","nonce":"1","blockHash":"0xc3787e902ec15d888cd8b2b9aa464b233e95189b85d56899f8effbf496bb85d6","from":"0xe24fb4d428e85b808743c096dd62f63cc87be881","contractAddress":"0xdac17f958d2ee523a2206206994597c13d831ec7","to":"0x0418f374f25edab13d38a7d82b445ce9934bfc12","value":"180000000","tokenName":"Tether USD","tokenSymbol":"USDT","tokenDecimal":"6","transactionIndex":"221","gas":"72197","gasPrice":"119900000000","gasUsed":"56197","cumulativeGasUsed":"8987230","input":"deprecated","confirmations":"312738"},
         * {"blockNumber":"11556019","timeStamp":"1609340106","hash":"0xf9fcbce7fac3df00f6ab7979a1af62c9e43cf8a1034856271927b496fd0587ae","nonce":"7887","blockHash":"0x0521d364268f56142bdfd14d2a6b2186dbe6b37c905c387946859c3beabf8d34","from":"0x0418f374f25edab13d38a7d82b445ce9934bfc12","contractAddress":"0xdac17f958d2ee523a2206206994597c13d831ec7","to":"0x8d90113a1e286a5ab3e496fbd1853f265e5913c6","value":"20000000","tokenName":"Tether USD","tokenSymbol":"USDT","tokenDecimal":"6","transactionIndex":"109","gas":"500000","gasPrice":"145000000000","gasUsed":"276496","cumulativeGasUsed":"7624084","input":"deprecated","confirmations":"311734"},
         * {"blockNumber":"11556032","timeStamp":"1609340293","hash":"0x4169c373874216dcafa3d1a713fa944202917bd38684ece01aa0b29c62f336ee","nonce":"1","blockHash":"0x1cbf7e25868ba88e379d6bdbe023278d28e71024d43bd75cab05565acf9978b7","from":"0x0418f374f25edab13d38a7d82b445ce9934bfc12","contractAddress":"0xdac17f958d2ee523a2206206994597c13d831ec7","to":"0x79fe9793e846a474614fa91f985f8b6b34e166eb","value":"160000000","tokenName":"Tether USD","tokenSymbol":"USDT","tokenDecimal":"6","transactionIndex":"162","gas":"60000","gasPrice":"124000001459","gasUsed":"41197","cumulativeGasUsed":"11317054","input":"deprecated","confirmations":"311721"},
         * {"blockNumber":"11589227","timeStamp":"1609779748","hash":"0xe70cbc720d2409b5c4f6500b5832f30f23db77b0ccc00d04febb883b006b7892","nonce":"1331532","blockHash":"0x0cbf9359c3d41779418fdd91aba0b31d86c834a201ceb4f2e7354fc2ba207742","from":"0x0a98fb70939162725ae66e626fe4b52cff62c2e5","contractAddress":"0xdac17f958d2ee523a2206206994597c13d831ec7","to":"0x0418f374f25edab13d38a7d82b445ce9934bfc12","value":"4196060000","tokenName":"Tether USD","tokenSymbol":"USDT","tokenDecimal":"6","transactionIndex":"191","gas":"100000","gasPrice":"214000000000","gasUsed":"56209","cumulativeGasUsed":"8364308","input":"deprecated","confirmations":"278526"},
         * {"blockNumber":"11589325","timeStamp":"1609780976","hash":"0x7d1687bccb09598cb032977a544d9beb57c5e095e6e5042d70fb14c2950a1e31","nonce":"2","blockHash":"0x9a09e7c6e47944670813c9c588a2d84abe49feca989815e338c422d397cd36b3","from":"0x0418f374f25edab13d38a7d82b445ce9934bfc12","contractAddress":"0xdac17f958d2ee523a2206206994597c13d831ec7","to":"0xcd1654a2eb816216c1c8d94a0f0f5ef1b562a4ba","value":"4122000000","tokenName":"Tether USD","tokenSymbol":"USDT","tokenDecimal":"6","transactionIndex":"154","gas":"60000","gasPrice":"111000001459","gasUsed":"41209","cumulativeGasUsed":"12409681","input":"deprecated","confirmations":"278428"}]}
         */


        //    查看全部     https://api.etherscan.io/api?module=account&action=tokentx&address=0x0418F374F25EdAb13D38a7D82b445cE9934Bfc12&startblock=0&endblock=999999999&sort=asc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M
        // 查看分页数据    https://api.etherscan.io/api?module=account&action=tokentx&address=0x0418F374F25EdAb13D38a7D82b445cE9934Bfc12&page=1&offset=100&sort=asc&apikey=JYNM1VJSXN8JE6JCY5M9JGKBDB7KPJDC5M
       /* for (int a = 0; a <= 100; a++) {
            ThreadUtil.execute(IP::run);

        }*/


        //https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/
        // api.php?query=27.192.0.0&co=&resource_id=5809&t=1607390613198&ie=utf8&oe=gbk&
        // cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206407212602169667_1607389234299&_=1607389234305


    }

    private static void run() {
        String ip = "27.192.0.0";
        String strUrl = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=";
        String centerUrl = "&co=&resource_id=5809&t=";
        String subUrl = "&ie=utf8&oe=gbk&cb=op_aladdin_callback&format=json&tn=baidu&cb=jQuery110206407212602169667_1607389234299";
        String timeUrl = "&_=";
        String url = strUrl + ip + centerUrl + System.currentTimeMillis() + subUrl + timeUrl + System.currentTimeMillis();
        String s = HttpUtil.get(url);
        System.out.println(UnicodeUtil.toString(s));
    }
}