package egovframework.com.fivemlist.service;

public class CarPriceVO {
    private int id;
    private String name;
    private String speed;
    private String minPrice;
    private String maxPrice;
    private String imageUrl;

    // 숫자형 가격을 위한 필드
    private long minPriceValue;
    private long maxPriceValue;

    public CarPriceVO() {}

    public CarPriceVO(int id, String name, String speed, String minPrice, String maxPrice, String imageUrl) {
        this.id = id;
        this.name = name;
        this.speed = speed;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.imageUrl = imageUrl;
        
        // 가격 변환
        this.minPriceValue = parsePrice(minPrice);  // 문자열을 숫자 형식으로 변환
        this.maxPriceValue = parsePrice(maxPrice);  // 문자열을 숫자 형식으로 변환
    }

    // Getter, Setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getSpeed() {
        return speed;
    }
    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getMinPrice() {
        return minPrice;
    }
    public void setMinPrice(long minPrice) {
        this.minPrice = String.valueOf(minPrice);
        this.minPriceValue = minPrice;  // 가격 변경 시 다시 숫자로 변환
    }

    public String getMaxPrice() {
        return maxPrice;
    }
    public void setMaxPrice(long maxPrice) {
        this.maxPrice = String.valueOf(maxPrice);  // String으로 수정
        this.maxPriceValue = maxPrice;  // 가격 변경 시 다시 숫자로 변환
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // 숫자 형식으로 가격을 변환하는 메서드
    public long parsePrice(String priceStr) {
        if (priceStr == null || priceStr.isEmpty()) return 0L;  // 빈 값이면 0 반환

        priceStr = priceStr.trim();  // 앞뒤 공백 제거

        // "측정불가" 문자열 처리
        if ("측정불가".equals(priceStr)) {
            return Long.MAX_VALUE;  // "측정불가"는 최대 값으로 처리
        }

        // 단위별로 처리
        if (priceStr.contains("억원")) {
            priceStr = priceStr.replace("억원", "").trim();  // "억원" 제거
            try {
                long num = Long.parseLong(priceStr);  // 숫자 변환
                return num * 100_000_000L;  // 억 단위로 변환 (1억원 = 100,000,000L)
            } catch (NumberFormatException e) {
                return 0L;  // 숫자로 변환 불가하면 0 반환
            }
        } else if (priceStr.contains("만원")) {
            priceStr = priceStr.replace("만원", "").trim();  // "만원" 제거
            try {
                long num = Long.parseLong(priceStr);  // 숫자 변환
                return num * 10_000L;  // 만원 단위로 변환 (1만원 = 10,000L)
            } catch (NumberFormatException e) {
                return 0L;  // 숫자로 변환 불가하면 0 반환
            }
        } else if (priceStr.contains("천만원")) {
            priceStr = priceStr.replace("천만원", "").trim();  // "천만원" 제거
            try {
                long num = Long.parseLong(priceStr);  // 숫자 변환
                return num * 1_000_000L;  // 천만원 단위로 변환 (1천만원 = 1,000,000L)
            } catch (NumberFormatException e) {
                return 0L;  // 숫자로 변환 불가하면 0 반환
            }
        } else if (priceStr.contains("조")) {
            priceStr = priceStr.replace("조", "").trim();  // "조" 제거
            try {
                long num = Long.parseLong(priceStr);  // 숫자 변환
                return num * 1_000_000_000_000L;  // 조 단위로 변환 (1조 = 1,000,000,000,000L)
            } catch (NumberFormatException e) {
                return 0L;  // 숫자로 변환 불가하면 0 반환
            }
        } else if (priceStr.matches("\\d+")) {
            // 단위가 없으면 그냥 숫자로 반환 (예: "5000000000" 같은 값)
            return Long.parseLong(priceStr);
        }

        return 0L;  // 알 수 없는 단위일 경우 0 반환
    }

    // 숫자형 가격 반환
    public long getMinPriceValue() {
        return minPriceValue;
    }

    public long getMaxPriceValue() {
        return maxPriceValue;
    }
}
