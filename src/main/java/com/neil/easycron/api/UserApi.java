package com.neil.easycron.api;
import java.util.UUID;

import com.neil.easycron.bo.response.JsonEntity;
import com.neil.easycron.bo.user.LoginRequestBo;
import com.neil.easycron.utils.ResponseHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/api")
@CrossOrigin
public class UserApi {
    @PostMapping("login")
    public JsonEntity<String> login(@RequestBody LoginRequestBo loginRequestBo) {
        return ResponseHelper.createInstance(UUID.randomUUID().toString().replace("-",""));
    }
}
