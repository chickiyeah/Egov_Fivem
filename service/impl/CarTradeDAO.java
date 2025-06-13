package egovframework.com.fivemlist.service.impl;

import org.egovframe.rte.psl.dataaccess.EgovAbstractMapper;
import org.springframework.stereotype.Repository;

import egovframework.com.fivemlist.service.CarTradeVO;

import java.util.List;

@Repository("carTradeDAO")
public class CarTradeDAO extends EgovAbstractMapper {

    public List<CarTradeVO> selectCarTradeList(CarTradeVO vo) {
        return selectList("CarTradeDAO.selectCarTradeList", vo);
    }

    public boolean insertCarTrade(CarTradeVO vo) {
        return insert("CarTradeDAO.insertCarTrade", vo) > 0;
    }

    public boolean updateCarTrade(CarTradeVO vo) {
        return update("CarTradeDAO.updateCarTrade", vo) > 0;
    }

    public boolean deleteCarTrade(CarTradeVO vo) {
        return delete("CarTradeDAO.deleteCarTrade", vo) > 0;
    }
}
