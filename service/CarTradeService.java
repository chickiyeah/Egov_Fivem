package egovframework.com.fivemlist.service;

import java.util.List;

public interface CarTradeService {
    List<CarTradeVO> selectCarTradeList(CarTradeVO vo) throws Exception;
    boolean insertCarTrade(CarTradeVO vo) throws Exception;
    boolean updateCarTrade(CarTradeVO vo) throws Exception;
    boolean deleteCarTrade(CarTradeVO vo) throws Exception;
}
