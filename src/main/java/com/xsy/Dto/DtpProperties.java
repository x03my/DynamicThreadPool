package com.xsy.Dto;


import lombok.Data;

import java.util.List;




@Data
public class DtpProperties {

    private List<ThreadPoolProperties> executors;

}
