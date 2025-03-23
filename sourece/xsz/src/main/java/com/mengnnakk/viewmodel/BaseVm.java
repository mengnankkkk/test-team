package com.mengnnakk.viewmodel;

import com.mengnnakk.base.BaseApiController;
import com.mengnnakk.utility.ModelMapperSingle;
import org.modelmapper.ModelMapper;

public class BaseVm {
    protected static ModelMapper modelMapper = ModelMapperSingle.Instance();

    public static ModelMapper getModelMapper(){
        return modelMapper;
    }
    public static void setModelMapper(ModelMapper modelMapper){
        BaseVm.modelMapper = modelMapper;
    }
}
