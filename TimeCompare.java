package UsefulFunction;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import Entity.Repo_Log;

@SuppressWarnings("rawtypes")
public class TimeCompare implements Comparator{
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss,SSS");
	
	@Override
	public int compare(Object o1, Object o2) {
		Repo_Log t1 = (Repo_Log) o1;
		Repo_Log t2 = (Repo_Log) o2;
		
     //   return t1.getTradetime().compareTo(t2.getTradetime());  // 時間格式不好，不然可以直接這樣比較
		Date d1, d2;
        try {
            d1 = format.parse(t1.getTime());
            d2 = format.parse(t2.getTime());
        } catch (ParseException e) {
            // 解析出錯，則不進行排序
            return 0;
        }
        if(d1.equals(d2)) {
        	return 0;
        } else if (d1.before(d2)) {
            return -1;
        } else {
            return 1;
        }
	}

}
