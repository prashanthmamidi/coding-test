package uk.sky;

public class LogExtract {

    private Long timeStamp;
    private String countryCode;
    private Long responseTime;

    public LogExtract(Long timeStamp, String countryCode, Long responseTime) {
        this.timeStamp = timeStamp;
        this.countryCode = countryCode;
        this.responseTime = responseTime;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogExtract)) return false;

        LogExtract that = (LogExtract) o;

        if (timeStamp != null ? !timeStamp.equals(that.timeStamp) : that.timeStamp != null) return false;
        if (countryCode != null ? !countryCode.equals(that.countryCode) : that.countryCode != null) return false;
        return !(responseTime != null ? !responseTime.equals(that.responseTime) : that.responseTime != null);

    }

    @Override
    public int hashCode() {
        int result = timeStamp != null ? timeStamp.hashCode() : 0;
        result = 31 * result + (countryCode != null ? countryCode.hashCode() : 0);
        result = 31 * result + (responseTime != null ? responseTime.hashCode() : 0);
        return result;
    }
}
