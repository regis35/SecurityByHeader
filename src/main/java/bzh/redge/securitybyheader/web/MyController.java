package bzh.redge.securitybyheader.web;

import bzh.redge.securitybyheader.security.ClientDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class MyController {

    @GetMapping("/index")
    public String index() {
        return "Welcome to index";
    }

    @GetMapping("/plot")
    public String plot() {
        return "Welcome to hello word";
    }
}
