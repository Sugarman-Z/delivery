package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.Employee;
import com.zgy.delivery.mapper.EmployeeMapper;
import com.zgy.delivery.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
