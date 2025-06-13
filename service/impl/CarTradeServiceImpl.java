package egovframework.com.fivemlist.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.com.fivemlist.service.CarTradeService;
import egovframework.com.fivemlist.service.CarTradeVO;

import java.util.List;

@Service("carTradeService")
public class CarTradeServiceImpl implements CarTradeService {

    @Autowired
    private CarTradeDAO carTradeDAO;

    @Override
    public List<CarTradeVO> selectCarTradeList(CarTradeVO vo) throws Exception {
        return carTradeDAO.selectCarTradeList(vo);
    }

    @Override
    public boolean insertCarTrade(CarTradeVO vo) throws Exception {
        return carTradeDAO.insertCarTrade(vo);
    }

    @Override
    public boolean updateCarTrade(CarTradeVO vo) throws Exception {
        return carTradeDAO.updateCarTrade(vo);
    }

    @Override
    public boolean deleteCarTrade(CarTradeVO vo) throws Exception {
        return carTradeDAO.deleteCarTrade(vo);
    }
}
