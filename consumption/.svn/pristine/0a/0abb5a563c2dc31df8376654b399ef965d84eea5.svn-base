package trong.lixco.com.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.annotations.SerializedName;

import lixco.com.common.JsonParserUtil;
import lixco.com.entity.MyLogDMS;
import lixco.com.interfaces.IInvoiceDetailService;
import lixco.com.interfaces.IInvoiceService;
import lixco.com.loaddata.InvoiceDMS;
import lixco.com.service.MyLogDMSService;

public class CallAPI {
	public static String dsdonHangDMS(String taikhoan, Date sDate, Date eDate) throws Exception {
		String tungay = MyUtil.chuyensangStr(sDate);
		if ("".equals(tungay))
			tungay = " ";
		String denngay = MyUtil.chuyensangStr(eDate);
		if ("".equals(denngay))
			denngay = " ";
		String urlParameters = "cm=taidanhsach&dt=" + taikhoan + "," + tungay + "," + denngay;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS + "donhangnpp");
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket quar
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket quar
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		}
	}

	public static String xacnhandonHangDMS(long iddonhang) throws Exception {
		String urlParameters = "cm=xacnhandonhang&dt=" + iddonhang;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS + "donhangnpp");
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		}

	}

	public static String boxacnhandonHangDMS(long iddonhang) throws Exception {
		String urlParameters = "cm=boxacnhandonhang&dt=" + iddonhang;
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
		int postDataLength = postData.length;
		URL url = new URL(StaticPath.pathDMS + "donhangnpp");
		if (StaticPath.pathDMS.contains("https")) {
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} else {
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("token", StaticPath.tokenDMS);
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.setUseCaches(false);
			OutputStream os = conn.getOutputStream();
			try (DataOutputStream wr = new DataOutputStream(os)) {
				wr.write(postData);
				wr.flush();
				wr.close();
			} finally {
				os.close();
			}
			// co ket qua
			try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		}

	}

	// ===== CONFIG =====
	private static final int CONNECT_TIMEOUT_MS = 20_000;   // 20s
	private static final int READ_TIMEOUT_MS    = 180_000;   // 180s
	private static final int BATCH_SIZE_INVOICE = 10;      // 10 tuỳ DMS
	private static final int BATCH_SIZE_DELETE  = 10;
	private static final int PARALLELISM        = 4;        // 3~5 an toàn
	private static final int MAX_RETRY          = 1;
	private static final long BASE_BACKOFF_MS   = 800;      // exponential backoff
	private static final String LOG_SOURCE      = "naptudong"; // như cũ

	public static String sendInvoiceToDMS(
            List<InvoiceDMS> rows,
            IInvoiceService invoiceService,
            IInvoiceDetailService invoiceDetailService, // Không dùng nhưng giữ lại theo signature
            MyLogDMSService logSvc) throws InterruptedException {

        if (rows == null || rows.isEmpty()) {
            if (logSvc != null) {
                logSvc.create(new MyLogDMS("dongbohoadon", true, LOG_SOURCE,
                        "Không có dữ liệu để đồng bộ", 0, "(summary)", "0 record"));
            }
            return "0 batch (empty)";
        }

        final String url = StaticPath.pathDMS + "nhaphang/receivelist";
        final String token = StaticPath.tokenDMS;

        // 1. Chia lô dữ liệu (batching)
        List<List<InvoiceDMS>> batches = chunk(rows, BATCH_SIZE_INVOICE);
        
        ExecutorService pool = Executors.newFixedThreadPool(PARALLELISM);
        List<Future<String>> futs = new ArrayList<>(batches.size());
        // Đếm tổng số hóa đơn được cập nhật thành công (trên DB Client)
        final AtomicInteger successCount = new AtomicInteger(0);

        int batchNo = 0;
        for (List<InvoiceDMS> batch : batches) {
            final int thisBatchNo = batchNo++;
            
            // 2. Chuyển đổi batch thành JSON
            final String json = JsonParserUtil.gsonnew.toJson(batch); 
            final String idemKey = "inv:" + Integer.toHexString(json.hashCode());

            futs.add(pool.submit(() -> {
                for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
                    try {
                        // 3. Gửi dữ liệu lên DMS
                        HttpResult http = postJsonWithStatus(url, json, token, idemKey);

                        // --- ĐIỀU CHỈNH 1: Xử lý lỗi HTTP nghiêm trọng (Khác 2xx) ---
                        if (!http.is2xx()) {
                            if (shouldRetry(http.code) && attempt < MAX_RETRY) {
                                sleepBackoff(attempt);
                                continue;
                            }
                            // Log FAIL cho lỗi HTTP
                            if (logSvc != null) {
                                logSvc.create(new MyLogDMS(
                                        "dongbohoadon", false, LOG_SOURCE,
                                        "Batch#" + thisBatchNo + " HTTP " + http.code + ". Lỗi hệ thống/Parsing.",
                                        http.code, "(batch#" + thisBatchNo + ")",
                                        truncate(http.body, 1500)
                                ));
                            }
                            return "[FAIL] batch#" + thisBatchNo + " size=" + batch.size()
                                    + " http=" + http.code;
                        }

                        // --- ĐIỀU CHỈNH 2: Xử lý OK (200/201) - Phân tích chi tiết JSON body ---
                        String resp = http.body;
                        List<InvoiceProcessResult> results = parseResponseResults(resp);
                        
                        List<Long> successIds = new ArrayList<>();
                        List<String> errorMessages = new ArrayList<>();

                        // Phân loại ID thành công và lỗi
                        for (InvoiceProcessResult result : results) {
                            if ("OK".equalsIgnoreCase(result.status) && result.sourceIdinvoice != null) {
                                successIds.add(result.sourceIdinvoice);
                            } else if ("ERROR".equalsIgnoreCase(result.status)) {
                                errorMessages.add("ID=" + result.sourceIdinvoice + ": " + truncate(result.message, 150));
                            }
                        }

                        // --- CẬP NHẬT TRẠNG THÁI CHỈ VỚI ID THÀNH CÔNG ---
                        int updated = 0;
                        for (Long id : successIds) {
                            try {
                                if (invoiceService != null) {
                                    // Cập nhật trạng thái 'đã gửi DMS' cho hóa đơn
                                    invoiceService.updateSendDMS(id);
                                    updated++;
                                }
                            } catch (Exception exUpdate) {
                                if (logSvc != null) {
                                    logSvc.create(new MyLogDMS(
                                            "dongbohoadon", false, LOG_SOURCE,
                                            "Lỗi updateSendDMS cho id=" + id,
                                            0, String.valueOf(id), truncate(exUpdate.getMessage(), 500)
                                    ));
                                }
                            }
                        }
                        successCount.addAndGet(updated);

                        // --- Log KẾT QUẢ Batch ---
                        boolean batchSuccess = errorMessages.isEmpty();
                        String summary = String.format("OK=%d, ERROR=%d.", updated, errorMessages.size());
                        String detail = summary + " Errors: " + truncate(errorMessages.toString(), 1500);

                        if (logSvc != null) {
                            logSvc.create(new MyLogDMS(
                                    "dongbohoadon", batchSuccess, LOG_SOURCE,
                                    "Batch#" + thisBatchNo + (batchSuccess ? " OK." : " CÓ LỖI NGHIỆP VỤ.") + " size=" + batch.size(),
                                    http.code, "(batch#" + thisBatchNo + ")", detail
                            ));
                        }
                        
                        return "[OK] batch#" + thisBatchNo + " size=" + batch.size() 
                               + " success=" + updated + " fail=" + errorMessages.size();

                    } catch (IOException e) {
                        // Xử lý lỗi IO (kết nối mạng)
                        if (attempt == MAX_RETRY) {
                            if (logSvc != null) {
                                logSvc.create(new MyLogDMS(
                                        "dongbohoadon", false, LOG_SOURCE,
                                        "Lỗi batch#" + thisBatchNo + ". Lỗi IO mạng.",
                                        0, "(batch#" + thisBatchNo + ")", truncate(e.getMessage(), 1000)
                                ));
                            }
                            return "[FAIL] batch#" + thisBatchNo + " size=" + batch.size() + " err=" + e.getMessage();
                        }
                        sleepBackoff(attempt);
                    } catch (Exception ex) {
                         // Xử lý lỗi chung khác (Ví dụ: Lỗi Parsing Response)
                        if (logSvc != null) {
                            logSvc.create(new MyLogDMS(
                                    "dongbohoadon", false, LOG_SOURCE,
                                    "Lỗi xử lý batch#" + thisBatchNo + " (Lỗi Parsing/Runtime Client)",
                                    0, "(batch#" + thisBatchNo + ")", truncate(ex.getMessage(), 1000)
                            ));
                        }
                        return "[FAIL] batch#" + thisBatchNo + " (general error)";
                    }
                }
                return "[FAIL] batch#" + thisBatchNo + " (unexpected)";
            }));
        }

        // 5. Tổng hợp kết quả
        StringBuilder sum = new StringBuilder("INVOICE_SYNC:");
        int ok = 0, fail = 0;
        
        // Dựa trên kết quả trả về từ Future (vẫn dùng [OK] và [FAIL] để đếm)
        for (Future<String> f : futs) {
            try {
                String r = f.get();
                sum.append("\n").append(r);
                if (r.startsWith("[OK]")) ok++; else fail++;
            } catch (Exception ex) {
                fail++;
                sum.append("\n[FAIL] exception: ").append(ex.getMessage());
                // ... (Log lỗi exception khi gom kết quả giữ nguyên) ...
            }
        }
        pool.shutdown();

        String rs = String.format("OK_Batches=%d FAIL_Batches=%d Successful_Invoices=%d%s", ok, fail, successCount.get(), sum.toString());
        
        // Log Summary cuối cùng
        if (logSvc != null) {
            // Tổng thể thành công nếu không có batch nào thất bại hoàn toàn (HTTP != 200)
            boolean overallSuccess = (fail == 0); 
            String message = (overallSuccess ? "Đồng bộ hoá đơn hoàn tất." : "Đồng bộ hoá đơn có lỗi nghiêm trọng.")
                    + " Tổng cộng " + successCount.get() + " hóa đơn thành công. Summary: " + truncate(rs, 800);
            logSvc.create(new MyLogDMS("dongbohoadon", overallSuccess, LOG_SOURCE, message, 0, "(summary)", rs));
        }
        return rs;
    }
	
	public static List<List<InvoiceDMS>> chunkInvoice(List<InvoiceDMS> list, int chunkSize) {
	    if (chunkSize <= 0) {
	        throw new IllegalArgumentException("chunkSize must be greater than 0");
	    }
	    List<List<InvoiceDMS>> batches = new ArrayList<>();
	    for (int i = 0; i < list.size(); i += chunkSize) {
	        int end = Math.min(list.size(), i + chunkSize);
	        batches.add(list.subList(i, end));
	    }
	    return batches;
	}


	public static String sendDeletionToDMS(
	        List<String> idrefs,
	        IInvoiceService invoiceService,
	        MyLogDMSService logSvc) {

	    if (idrefs == null || idrefs.isEmpty()) {
	        if (logSvc != null) {
	            logSvc.create(new MyLogDMS("xoahoadon", true, LOG_SOURCE,
	                    "Không có idRef để xoá", 0, "(summary)", "0 idRef"));
	        }
	        return "0 batch (empty)";
	    }

	    final String url   = StaticPath.pathDMS + "nhaphang/deletereflist";
	    final String token = StaticPath.tokenDMS;

	    List<List<String>> batches = chunk(idrefs, BATCH_SIZE_DELETE);
	    java.util.concurrent.ExecutorService pool = java.util.concurrent.Executors.newFixedThreadPool(PARALLELISM);
	    List<java.util.concurrent.Future<String>> futs = new ArrayList<>(batches.size());

	    int batchNo = 0;
	    for (List<String> batch : batches) {
	        final int thisBatchNo = batchNo++;
	        final String json     = JsonParserUtil.gsonnew.toJson(batch);
	        final String idemKey  = "del:" + Integer.toHexString(json.hashCode());
	        final String idsPreview = truncate(batch.toString(), 200);

	        futs.add(pool.submit(() -> {
	            for (int attempt = 0; attempt <= MAX_RETRY; attempt++) {
	                try {
	                    HttpResult http = postJsonWithStatus(url, json, token, idemKey);

	                    if (!http.is2xx()) {
	                        if (shouldRetry(http.code) && attempt < MAX_RETRY) {
	                            sleepBackoff(attempt);
	                            continue;
	                        }
	                        if (logSvc != null) {
	                            logSvc.create(new MyLogDMS(
	                                    "xoahoadon", false, LOG_SOURCE,
	                                    "Batch#" + thisBatchNo + " HTTP " + http.code + ". IDs: " + idsPreview,
	                                    http.code, "(batch#" + thisBatchNo + ")",
	                                    truncate(http.body, 1500)
	                            ));
	                        }
	                        return "[FAIL] batch#" + thisBatchNo + " size=" + batch.size()
	                                + " http=" + http.code;
	                    }

	                    // 2xx: OK
	                    if (logSvc != null) {
	                        logSvc.create(new MyLogDMS(
	                                "xoahoadon", true, LOG_SOURCE,
	                                "Batch#" + thisBatchNo + " OK xoá " + batch.size() + " idRef",
	                                0, "(batch#" + thisBatchNo + ")",
	                                "IDs: " + idsPreview + " | RESP: " + truncate(http.body, 1000)
	                        ));
	                    }
	                    return "[OK] batch#" + thisBatchNo + " size=" + batch.size();

	                } catch (IOException e) {
	                    if (attempt == MAX_RETRY) {
	                        if (logSvc != null) {
	                            logSvc.create(new MyLogDMS(
	                                    "xoahoadon", false, LOG_SOURCE,
	                                    "Lỗi batch#" + thisBatchNo + " khi xoá. IDs: " + idsPreview,
	                                    0, "(batch#" + thisBatchNo + ")", truncate(e.getMessage(), 1000)
	                            ));
	                        }
	                        return "[FAIL] batch#" + thisBatchNo + " size=" + batch.size()
	                                + " err=" + e.getMessage();
	                    }
	                    sleepBackoff(attempt);
	                }
	            }
	            return "[FAIL] batch#" + thisBatchNo + " (unexpected)";
	        }));
	    }

	    StringBuilder sum = new StringBuilder("INVOICE_DELETE:");
	    int ok = 0, fail = 0;
	    for (java.util.concurrent.Future<String> f : futs) {
	        try {
	            String r = f.get();
	            sum.append("\n").append(r);
	            if (r.startsWith("[OK]")) ok++; else fail++;
	        } catch (Exception ex) {
	            fail++;
	            sum.append("\n[FAIL] exception: ").append(ex.getMessage());
	            if (logSvc != null) {
	                logSvc.create(new MyLogDMS(
	                        "xoahoadon", false, LOG_SOURCE,
	                        "Exception khi gom kết quả batch",
	                        0, "(summary)", truncate(ex.getMessage(), 1000)
	                ));
	            }
	        }
	    }
	    pool.shutdown();

	    String rs = String.format("OK=%d FAIL=%d%s", ok, fail, sum.toString());
	    if (logSvc != null) {
	        boolean success = (ok > 0 && fail == 0);
	        String message  = (success ? "Xoá hoá đơn thành công" : "Xoá hoá đơn có lỗi")
	                + ". Summary: " + truncate(rs, 800);
	        logSvc.create(new MyLogDMS("xoahoadon", success, LOG_SOURCE, message, 0, "(summary)", rs));
	    }
	    return rs;
	}


	// ===== HELPERS =====
	private static void sleepBackoff(int attempt) {
	    long base = (long) (BASE_BACKOFF_MS * Math.pow(2, attempt));
	    long jitter = ThreadLocalRandom.current().nextLong(0, 250);
	    try { Thread.sleep(base + jitter); } catch (InterruptedException ignored) {}
	}

	private static String truncate(String s, int max) {
	    if (s == null) return "";
	    return s.length() <= max ? s : s.substring(0, max) + "...";
	}

	private static <T> List<List<T>> chunk(List<T> all, int size) {
	    List<List<T>> out = new ArrayList<>();
	    if (all == null || all.isEmpty()) return out;
	    for (int i = 0; i < all.size(); i += size) {
	        out.add(all.subList(i, Math.min(all.size(), i + size)));
	    }
	    return out;
	}

	private static String postJson(String url, String json, String token, String idemKey) throws IOException {
	    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
	    conn.setReadTimeout(READ_TIMEOUT_MS);
	    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

	    // Nếu DMS yêu cầu Authorization thay vì header 'token', đổi dòng dưới cho phù hợp:
	    if (token != null && !token.isEmpty()) conn.setRequestProperty("token", token);

	    if (idemKey != null && !idemKey.isEmpty()) conn.setRequestProperty("Idempotency-Key", idemKey);
	    // Bật nếu DMS hỗ trợ nén:
	    // conn.setRequestProperty("Content-Encoding", "gzip");

	    try (OutputStream os = conn.getOutputStream()) {
	        // Nếu dùng GZIP:
	        // try (GZIPOutputStream gos = new GZIPOutputStream(os)) {
	        //     gos.write(json.getBytes(StandardCharsets.UTF_8));
	        // }
	        os.write(json.getBytes(StandardCharsets.UTF_8));
	    }

	    int code = conn.getResponseCode();
	    InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
	    String body;
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
	        StringBuilder sb = new StringBuilder(4096);
	        for (String line; (line = br.readLine()) != null; ) sb.append(line);
	        body = sb.toString();
	    }

	    // Quăng IOE để lớp trên retry với 429/5xx
	    if (code == 429 || code >= 500) throw new IOException("HTTP " + code + " " + body);
	    return body; // 2xx/4xx khác trả về cho caller tự decide
	}
	@SuppressWarnings("unchecked")
	private static List<String> extractSuccessInvoiceIds(String respJson) {
	    List<String> out = new ArrayList<>();
	    if (respJson == null || respJson.isEmpty()) return out;

	    try {
	        com.google.gson.JsonElement root = new com.google.gson.JsonParser().parse(respJson);

	        // Trường hợp 1: mảng kết quả trực tiếp
	        if (root.isJsonArray()) {
	            for (com.google.gson.JsonElement el : root.getAsJsonArray()) {
	                if (!el.isJsonObject()) continue;
	                com.google.gson.JsonObject o = el.getAsJsonObject();
	                boolean ok = o.has("success") ? o.get("success").getAsBoolean()
	                           : o.has("status")  ? "OK".equalsIgnoreCase(o.get("status").getAsString())
	                           : true; // nếu không có cờ, coi như ok
	                if (!ok) continue;
	                if (o.has("idinvoice") && !o.get("idinvoice").isJsonNull()) {
	                    out.add(o.get("idinvoice").getAsString());
	                }
	            }
	            return out;
	        }

	        // Trường hợp 2: object có trường data = array
	        if (root.isJsonObject()) {
	            com.google.gson.JsonObject obj = root.getAsJsonObject();
	            if (obj.has("data") && obj.get("data").isJsonArray()) {
	                for (com.google.gson.JsonElement el : obj.get("data").getAsJsonArray()) {
	                    if (!el.isJsonObject()) continue;
	                    com.google.gson.JsonObject o = el.getAsJsonObject();
	                    boolean ok = o.has("success") ? o.get("success").getAsBoolean()
	                               : o.has("status")  ? "OK".equalsIgnoreCase(o.get("status").getAsString())
	                               : true;
	                    if (!ok) continue;
	                    if (o.has("idinvoice") && !o.get("idinvoice").isJsonNull()) {
	                        out.add(o.get("idinvoice").getAsString());
	                    } else if (o.has("idRef") && !o.get("idRef").isJsonNull()) {
	                        out.add(o.get("idRef").getAsString());
	                    }
	                }
	                return out;
	            }
	            // Trường hợp 3: object nhưng không có data → thử lấy một trường đơn lẻ
	            if (obj.has("idinvoice")) out.add(obj.get("idinvoice").getAsString());
	            else if (obj.has("idRef")) out.add(obj.get("idRef").getAsString());
	        }
	    } catch (Exception ignore) {
	        // Không parse được → trả list rỗng, caller vẫn log batch OK nhưng không updateSendDMS
	    }
	    return out;
	}
	// ---- Helper giữ status + body ----
	private static final class HttpResult {
	    final int code;
	    final String body;
	    HttpResult(int code, String body) { this.code = code; this.body = body; }
	    boolean is2xx() { return code >= 200 && code < 300; }
	}

	// Retry với các mã nên thử lại (timeout/proxy/throttle/server)
	private static boolean shouldRetry(int code) {
	    return code == 408 || code == 429 || code >= 500;
	}

	// THAY cho postJson cũ: trả về cả status code + body
	private static HttpResult postJsonWithStatus(String url, String json, String token, String idemKey) throws IOException {
	    HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	    conn.setDoOutput(true);
	    conn.setRequestMethod("POST");
	    conn.setConnectTimeout(CONNECT_TIMEOUT_MS);
	    conn.setReadTimeout(READ_TIMEOUT_MS);
	    conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
	    if (token != null && !token.isEmpty()) conn.setRequestProperty("token", token);
	    if (idemKey != null && !idemKey.isEmpty()) conn.setRequestProperty("Idempotency-Key", idemKey);
	    // Nếu DMS hỗ trợ: conn.setRequestProperty("Content-Encoding", "gzip");

	    try (OutputStream os = conn.getOutputStream()) {
	        // Nếu dùng GZIP thì wrap GZIPOutputStream ở đây
	        os.write(json.getBytes(java.nio.charset.StandardCharsets.UTF_8));
	    }

	    int code = conn.getResponseCode();
	    InputStream is = (code >= 200 && code < 400) ? conn.getInputStream() : conn.getErrorStream();
	    String body;
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(is, java.nio.charset.StandardCharsets.UTF_8))) {
	        StringBuilder sb = new StringBuilder(4096);
	        for (String line; (line = br.readLine()) != null; ) sb.append(line);
	        body = sb.toString();
	    }
	    return new HttpResult(code, body);
	}
	
	private static List<InvoiceProcessResult> parseResponseResults(String resp) {
	    if (resp == null || resp.trim().isEmpty()) {
	        return new ArrayList<>();
	    }
	    try {
	        // 1. Định nghĩa kiểu dữ liệu cho lớp Wrapper
	        // KHÔNG dùng TypeToken cho List nữa, mà dùng trực tiếp lớp Wrapper
	        InvoiceProcessResponse response = 
	            JsonParserUtil.gsonnew.fromJson(resp, InvoiceProcessResponse.class);
	        
	        // 2. Kiểm tra và trả về mảng kết quả
	        if (response != null && response.results != null) {
	            return response.results;
	        }
	    } catch (Exception e) {
	        // Log lỗi parse JSON nếu cần thiết
	        System.err.println("Lỗi khi parse JSON response: " + e.getMessage());
	    }

	    return new ArrayList<>();
	}
	class InvoiceProcessResponse {
	    public String status;
	    public Integer totalProcessed;
	    public Integer successCount;
	    public Integer errorCount;
	    public List<InvoiceProcessResult> results; // <-- Mảng chứa kết quả chi tiết
	}

	// Lớp kết quả chi tiết (cập nhật tên trường)
	class InvoiceProcessResult {
	    public String status;
	    public String op;
	    public Long id;
	    public Integer detailCount;
	    public String message; 
	    
	    // Sử dụng @SerializedName để ánh xạ từ tên JSON (idinvoice) sang tên biến Java (sourceIdinvoice)
	    @SerializedName("idinvoice")
	    public Long sourceIdinvoice; 
	}
}

