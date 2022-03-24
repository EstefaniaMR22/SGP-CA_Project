package controller;

import controller.validator.IValidatorControlFX;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class it must be used in each interface in which data can be entered.
 */
public class ValidatorController extends Controller {
    private Map<IValidatorControlFX, Boolean> controlsMap = new LinkedHashMap<>();

    /***
     * This method should be used to add any component Control to the LinkedHashMap.
     * <p>
     *     The LinkedHashMap is a ordered list that contains every ControlFX.
     *     This map is used like a switch that holds a Control like Key and
     *     a Boolean like value.
     * </p>
     * @param control the component in the GUI.
     * @param value the initial flag value (true if the component input data is valid otherwise it should be false).
     */
    public void addComponentToValidator(IValidatorControlFX control, Boolean value) {
        controlsMap.put(control, value);
    }

    /***
     * Clear the LinkedHashMap
     */
    public void clearMap() {
        controlsMap.clear();
    }

    /***
     * This method clear the style to all the elements defined in LinkedHashMap Controlsmap.
     */
    public void clearStyleToInputs() {
        for( Map.Entry<IValidatorControlFX, Boolean> entry : controlsMap.entrySet() ) {
                entry.getKey().clearStyleClass();
                controlsMap.put(entry.getKey(), false);
        }
    }

    /***
     * This method initialize a Listener to every component.
     * <p> This method must be used when you have finished entering the components using
     *      the addComponentToValidator. Once this done, this method will generate a
     *      ChangeListener to everyComponent defined in controlsMap.
     * </p>
     */
    public void initListenerToControls() {
        for( Map.Entry<IValidatorControlFX, Boolean> controlEntry : controlsMap.entrySet() ) {
            controlEntry.getKey().initListener();
        }
    }

    /***
     * This method check the value for every Control Component.
     * @return true if ALL Contorl Components has its flag in true otherwise false.
     * @throws Exception only if it happend a exception.
     */
    public boolean validateInputs() throws Exception {
        boolean dataInputValid = true;
        for( Map.Entry<IValidatorControlFX, Boolean> entry : controlsMap.entrySet() ) {
            if( !entry.getValue() ){
                dataInputValid = false;
                entry.getKey().setInvalidStyleClass();
                // With this break, only the closest input will be market in red.
                //break;
            }
        }
        return dataInputValid;
    }
}
