package top.thesky341.blogapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.thesky341.blogapi.entity.Admin;
import top.thesky341.blogapi.mapper.AdminMapper;
import top.thesky341.blogapi.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin addAdmin(Admin admin) {
        adminMapper.addAdmin(admin);
        int id = admin.getId();
        return adminMapper.getAdminById(id);
    }
}