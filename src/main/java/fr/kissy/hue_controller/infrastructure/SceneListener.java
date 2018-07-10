package fr.kissy.hue_controller.infrastructure;

import com.philips.lighting.hue.listener.PHSceneListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHScene;
import fr.kissy.hue_controller.interfaces.BridgeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SceneListener implements PHSceneListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SceneListener.class);
    private SimpMessagingTemplate template;

    @Autowired
    public SceneListener(SimpMessagingTemplate template) {
        this.template = template;
    }

    @Override
    public void onScenesReceived(List<PHScene> list) {

    }

    @Override
    public void onSceneReceived(PHScene phScene) {

    }

    @Override
    public void onSuccess() {
        template.convertAndSend(BridgeController.SCENES_TOPIC, PHHueSDK.getInstance().getSelectedBridge().getResourceCache().getScenes().values());
    }

    @Override
    public void onError(int i, String s) {
        LOGGER.error("Error while updating scene : " + s);
    }

    @Override
    public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {

    }
}
