package jp.androidapp.libs.pluggablealarm;

import com.google.analytics.tracking.android.Tracker;

/**
 * ほぼ標準のandroid.util.Logと同じ機能だが、フラグを見てログ出力抑制を行う部分だけが違う。 フラグは将来的に拡張の可能性あり。
 * 足りない定数やメソッドは、android.util.Logと同等の範囲で増やして構わない。
 * 
 * @author maimuzo
 */
public class Log {
    public static boolean isDebugMode = true;
    private static int MAX_LOG_LENGTH = 300; // 文字数
    public static final int INFO = android.util.Log.INFO;
    private static Tracker sTracker;
    static {
        sTracker = null;
    }

    public static boolean isLoggable(final String tag,
                                     final int level) {
        return android.util.Log.isLoggable(tag, level);
    }

    public static void setTracker(Tracker t) {
        sTracker = t;
    }

    public static void d(final String tag,
                         final String msg) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.d(tag, msg);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.d(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.d(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
            } else {
                // 短ければそのまま出す
                android.util.Log.d(tag, msg);
            }
        }
    }

    public static void d(final String tag,
                         final String msg,
                         final Throwable tr) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.d(tag, msg, tr);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.d(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.d(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
                android.util.Log.d(tag, "(Back trace): ", tr);
            } else {
                // 短ければそのまま出す
                android.util.Log.d(tag, msg, tr);
            }
        }
    }

    public static void v(final String tag,
                         final String msg) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.v(tag, msg);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.v(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.v(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
            } else {
                // 短ければそのまま出す
                android.util.Log.v(tag, msg);
            }
        }
    }

    public static void i(final String tag,
                         final String msg) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.i(tag, msg);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.i(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.i(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
            } else {
                // 短ければそのまま出す
                android.util.Log.i(tag, msg);
            }
        }
    }

    public static void w(final String tag,
                         final String msg) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.w(tag, msg);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.w(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.w(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
            } else {
                // 短ければそのまま出す
                android.util.Log.w(tag, msg);
            }
        }
    }

    public static void w(final String tag,
                         final Throwable tr) {
        if (isDebugMode) {
            android.util.Log.w(tag, tr);
        }
    }

    public static void w(final String tag,
                         final String msg,
                         final Throwable tr) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.w(tag, msg, tr);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.w(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.w(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
                android.util.Log.w(tag, "(Back trace): ", tr);
            } else {
                // 短ければそのまま出す
                android.util.Log.w(tag, msg, tr);
            }
        }
    }

    public static void e(final String tag,
                         final String msg,
                         final Throwable tr) {
        if (isDebugMode) {
            if (null == msg) {
                // nullは任せる
                android.util.Log.e(tag, msg, tr);
            } else if (MAX_LOG_LENGTH < msg.length()) {
                // 長い場合は分割して出す
                android.util.Log.e(tag, "MULTI LINE LOG: total length: " + msg.length());
                int counter, start, end;
                String buffer;
                for (int i = 0; i * MAX_LOG_LENGTH < msg.length(); i++) {
                    start = i * MAX_LOG_LENGTH;
                    end = (i + 1) * MAX_LOG_LENGTH;
                    if (end > msg.length()) {
                        end = msg.length();
                    }
                    buffer = msg.substring(start, end);
                    counter = buffer.length();
                    android.util.Log.e(tag, "(" + start + " to " + end + ", length: " + counter + "): " + buffer);
                }
                android.util.Log.e(tag, "(Back trace): ", tr);
            } else {
                // 短ければそのまま出す
                android.util.Log.e(tag, msg, tr);
            }
        }
        if (null != sTracker) {
            sTracker.sendException(tr.getMessage(), false);
        }
    }
}
