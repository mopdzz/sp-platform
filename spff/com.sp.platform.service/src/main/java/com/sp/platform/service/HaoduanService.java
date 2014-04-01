package com.sp.platform.service;

import com.sp.platform.entity.Haoduan;
import com.sp.platform.dao.HaoduanDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User: yangl
 * Date: 13-5-26 下午3:47
 */
@Service
@Transactional
public class HaoduanService {

    @Autowired
    private HaoduanDao haoduanDao;

    @Transactional(readOnly = true)
    public List<Haoduan> getAll() {
        return haoduanDao.getAll();
    }

    @Transactional(readOnly = true)
    public List<Haoduan> getHaoduansGtId(int id) {
        return haoduanDao.getHaoduansGtId(id);
    }

    public void save(Haoduan haoduan){
        haoduanDao.save(haoduan);
    }
}
