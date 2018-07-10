package fr.kissy.hue_controller.interfaces;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHSchedule;
import com.philips.lighting.model.rule.PHRule;
import com.philips.lighting.model.sensor.PHSensor;
import fr.kissy.hue_controller.infrastructure.SceneListener;
import fr.kissy.hue_controller.model.ResourceIdentitifer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
public class DebugController {

    private SceneListener sceneListener;

    @Autowired
    public DebugController(SceneListener sceneListener) {
        this.sceneListener = sceneListener;
    }

    @RequestMapping("/debug")
    public void debug() {
        PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
        Map<String, PHSensor> allSchedules = selectedBridge.getResourceCache().getSensors();
        for (PHSensor phRule : allSchedules.values()) {
            System.out.println(phRule);
        }

    }

}