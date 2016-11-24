package fr.kissy.hue_controller.interfaces;

import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHLightState;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class EffectController {
    @MessageMapping("/light/enable")
    public void enable() {
        PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
        PHLightState newState = new PHLightState();
        newState.setOn(true);
        newState.setHue(0);
        newState.setSaturation(254);
        newState.setBrightness(254);
        selectedBridge.updateLightState(selectedBridge.getResourceCache().getLights().get("4"), newState);
    }

    @MessageMapping("/light/disable")
    public void disable() {
        PHBridge selectedBridge = PHHueSDK.getInstance().getSelectedBridge();
        PHLightState newState = new PHLightState();
        newState.setHue(0);
        newState.setSaturation(0);
        newState.setBrightness(254);
        selectedBridge.updateLightState(selectedBridge.getResourceCache().getLights().get("4"), newState);
    }
}