package com.zgy.delivery.dto;

import com.zgy.delivery.entity.Setmeal;
import com.zgy.delivery.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
