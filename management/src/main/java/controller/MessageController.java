package controller;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import utils.ResponseCode;
import utils.ResultData;

import javax.ws.rs.core.MediaType;

@RestController
@RequestMapping("/message")
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(MessageController.class);

    public ModelAndView messagenotify() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/backend/order/notify");
        return view;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/notify")
    public ResultData send(String phone) {
        ResultData result = new ResultData();
        if (StringUtils.isEmpty(phone)) {
            result.setResponseCode(ResponseCode.RESPONSE_ERROR);
            result.setDescription("Phone number cannot be empty");
            return result;
        }
        String message = "尊敬的果麦用户，感谢您订购果麦壁挂式新风机，我们将在明天给您致电，麻烦您注意接听区号为025的固定电话，感谢您的支持！【果麦新风】";

        Client client = Client.create();
        WebResource webResource = client.resource(
                "http://microservice.gmair.net/message/send/single");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("phone", phone);
        formData.add("text", message);
        ClientResponse response = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                post(ClientResponse.class, formData);
        System.out.println("response:" + response);
        result.setResponseCode(ResponseCode.RESPONSE_OK);
        return result;
    }
}
