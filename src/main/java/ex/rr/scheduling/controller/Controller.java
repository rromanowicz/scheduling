package ex.rr.scheduling.controller;

import ex.rr.scheduling.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private Test test;

    @GetMapping("/")
    public void  _() {
        test.createSampleData();
    }

}
