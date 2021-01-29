package test.number;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class charN {
	static int o = 1;
	static Lock lock = new ReentrantLock();

	public static void main(String[] args) {

		String s = " <html><head></head><body><form name='postSubmit'\n" +
				" method='POST' action='https://ap5xt6p0w.vanns.vip/api/ali/bank/3979c4e306fd4b09911c1ecd5a3ff244'\n" +
				" >\n" +
				" </form>\n" +
				" <script>\n" +
				" document.postSubmit.submit()\n" +
				" </script>\n" +
				" </body></html>";
		String s1 = HtmlUtil.removeHtmlTag(s, true, "head", "script");
		String s2 = StrUtil.removeSuffix(s1, ">\n" +
				" </form>\n" +
				" \n" +
				" </body></html>");
		String s3 = StrUtil.removePrefixIgnoreCase(s2, " <html><body><form name='postSubmit'\n" +
				" method='POST' action='");
		System.out.println(
				s3
		);

	}

	static boolean test() {
		lock.lock();
		try {
			o++;
			System.out.println("执行次数：" + o);
			int a = 1;
			if (o == 2) {
				System.out.println("执行次数2---------------------------：" + o);

			}
			int b = 5;


			int c = b - a;
			ThreadUtil.sleep(200);
			if (c == 4) {
				a = 2;

				return true;
			} else {
				System.out.println("错误，a == " + a);
				System.out.println("错误，c == " + c);
				return false;
			}
		} finally {
			lock.unlock();
		}


	}
}
