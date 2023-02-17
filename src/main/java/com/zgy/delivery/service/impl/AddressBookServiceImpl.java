package com.zgy.delivery.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zgy.delivery.entity.AddressBook;
import com.zgy.delivery.mapper.AddressBookMapper;
import com.zgy.delivery.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
