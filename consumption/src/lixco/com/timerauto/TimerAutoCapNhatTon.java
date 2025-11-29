package lixco.com.timerauto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.inject.Inject;

import lixco.com.common.JsonParserUtil;
import lixco.com.entity.HeThong;
import lixco.com.interfaces.IReportService;
import lixco.com.reportInfo.TonKhoThang;
import lixco.com.service.HeThongService;
import lixco.com.service.TonKhoThucTeService;
import trong.lixco.com.util.MyUtil;

import com.google.gson.JsonObject;

@Singleton
@Startup
@LocalBean
public class TimerAutoCapNhatTon {
	@Resource
	private TimerService timerService;
	@Inject
	private IReportService reportService;
	@Inject
	TonKhoThucTeService tonKhoThucTeService;
	@Inject
	HeThongService heThongService;

	@PostConstruct
	private void init() {
		setTime();
	}

	public void setTime() {
		Timer timer = null;
		Collection<Timer> timers = timerService.getTimers();
		Iterator<Timer> iterator = timers.iterator();
		while (iterator.hasNext()) {
			timer = iterator.next();
			timer.cancel();
		}
		TimerConfig timerConfig = new TimerConfig();
		timerConfig.setInfo("CalendarProcessAuto");

		String year = "*";
		String month = "*";
		String dayOfMonth = "*";
		String hour = "23";
		String minute = "1";
		ScheduleExpression schedule = new ScheduleExpression();
		schedule.year(year).month(month).dayOfMonth(dayOfMonth).hour(hour).minute(minute).second("1");
		timerService.createCalendarTimer(schedule, timerConfig);
	}

	@Timeout
	public void execute(Timer timer) {
		capnhattonkhothucte();
	}

	private void capnhattonkhothucte() {
		try {
			Date now = new Date();
			int date = now.getDate();
			if (date >= 3) {

				HeThong heThong = heThongService.findById(1l);
				if (heThong.isKiemton()) {

					int monthSearch = now.getMonth() + 1;
					int yearSearch = now.getYear() + 1900;
					JsonObject json = new JsonObject();
					json.addProperty("month", monthSearch);
					json.addProperty("year", yearSearch);
					json.addProperty("product_type_id", 0);
					json.addProperty("product_id", 0);
					json.addProperty("typep", -1);
					List<TonKhoThang> listTonKhoThang = new ArrayList<>();
					reportService.reportTonKhoThang(JsonParserUtil.getGson().toJson(json), listTonKhoThang);
					if (listTonKhoThang.size() > 0) {
						tonKhoThucTeService.saveOrUpdate(listTonKhoThang);
						System.out.println("Cập nhật tồn tự động: " + MyUtil.chuyensangStrHH(now));
					} else {
						System.out.println("Cập nhật tồn tự động: Không có dữ liệu cập nhật");
					}
				}else{
					System.out.println("Hệ thống không kiểm tồn");
				}
			} else {
				System.out.println("Không cập nhật tồn tự động khi ngày < 3 (" + MyUtil.chuyensangStrHH(now) + ")");
			}
		} catch (Exception e) {
			System.out.println("Xảy ra lỗi khi cập nhật tồn tự động");
			e.printStackTrace();

		}
	}
}