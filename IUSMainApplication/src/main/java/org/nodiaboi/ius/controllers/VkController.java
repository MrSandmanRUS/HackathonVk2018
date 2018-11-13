package org.nodiaboi.ius.controllers;

import com.vk.api.sdk.actions.Groups;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.groups.GroupFull;
import org.nodiaboi.ius.entity.IpCache;
import org.nodiaboi.ius.parsing.VkGroupTagsParser;
import org.nodiaboi.ius.service.EvaluateRecommendationsService;
import org.nodiaboi.ius.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class VkController {

    private VkGroupTagsParser vkGroupTagsParser = new VkGroupTagsParser();

    @Autowired
    private UserService userService;

    TransportClient transportClient = HttpTransportClient.getInstance();
    VkApiClient vk = new VkApiClient(transportClient);

    @CrossOrigin
    @GetMapping("/getVk")
    @ResponseBody
    public ResponseEntity<?> getVk(@RequestParam("code") String code, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        UserAuthResponse authResponse = null;
        try {
            authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(Integer.valueOf("6742720"), "mOczuMp7htx481kiecfj", "172.20.38.119:8081/getVk", code)
                    .execute();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

        IpCache.putLogin(httpServletRequest.getRemoteAddr(), actor.getId().toString());
        userService.createUser(actor.getId().toString(), null);
        System.out.println(actor.getId());
        try {
            httpServletResponse.sendRedirect("http://172.20.37.229:4200/getVk?code="+code);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Groups groups = new Groups(vk);
                    List<Integer> ids = null;
                    ids = groups.get(actor).execute().getItems();
                    StringBuilder sb = new StringBuilder();
                    for (Integer id : ids) {
                        String groupName = groups.getById(actor).groupIds(id.toString()).execute().get(0).getName();
                        Thread.sleep(1000);
                        sb.append(groupName + " ");
                    }
                    String result = sb.toString();
                    vkGroupTagsParser.LoadVkTagToPhoenixChicken(actor.getId().toString(), result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        return ResponseEntity.ok().body("success");
    }

}
