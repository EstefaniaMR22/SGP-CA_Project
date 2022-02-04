/*
 * @author Estefanía 
 * @versión v1.0
 * Last modification date: 17-06-2021
 */

package sgp.ca.domain;

public class AssistantRol{
    private int assistantRolKey;
    private String assistantName;
    private String roleAssistant;
    private int assistantNumber;

    public AssistantRol(int assistantRolKey, String assistantName, 
    String roleAssistant, int assistantNumber){
        this.assistantRolKey = assistantRolKey;
        this.assistantName = assistantName;
        this.roleAssistant = roleAssistant;
        this.assistantNumber = assistantNumber;
    }

    public AssistantRol(){
        
    }

    public int getAssistantRolKey(){
        return assistantRolKey;
    }

    public void setAssistantRolKey(int assistantRolKey){
        this.assistantRolKey = assistantRolKey;
    }

    public String getAssistantName(){
        return assistantName;
    }

    public void setAssistantName(String assistantName){
        this.assistantName = assistantName;
    }

    public String getRoleAssistant(){
        return roleAssistant;
    }

    public void setRoleAssistant(String roleAssistant){
        this.roleAssistant = roleAssistant;
    }

    public int getAssistantNumber(){
        return assistantNumber;
    }

    public void setAssistantNumber(int assistantNumber){
        this.assistantNumber = assistantNumber;
    }
}
