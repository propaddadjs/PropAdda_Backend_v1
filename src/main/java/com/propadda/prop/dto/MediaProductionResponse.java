// Author-Hemant Arora
package com.propadda.prop.dto;

public class MediaProductionResponse {

    private Integer mediaProductionId;
    private Boolean graphics;
    private Boolean photoshoot;
    private AgentResponse agent;
    private CommercialPropertyResponse comResponse;
    private ResidentialPropertyResponse resResponse;

        public Integer getMediaProductionId() {
        return mediaProductionId;
    }
    public void setMediaProductionId(Integer mediaProductionId) {
        this.mediaProductionId = mediaProductionId;
    }
    public Boolean getGraphics() {
        return graphics;
    }
    public void setGraphics(Boolean graphics) {
        this.graphics = graphics;
    }
    public Boolean getPhotoshoot() {
        return photoshoot;
    }
    public void setPhotoshoot(Boolean photoshoot) {
        this.photoshoot = photoshoot;
    }
    public AgentResponse getAgent() {
        return agent;
    }
    public void setAgent(AgentResponse agent) {
        this.agent = agent;
    }
    public CommercialPropertyResponse getComResponse() {
        return comResponse;
    }
    public void setComResponse(CommercialPropertyResponse comResponse) {
        this.comResponse = comResponse;
    }
    public ResidentialPropertyResponse getResResponse() {
        return resResponse;
    }
    public void setResResponse(ResidentialPropertyResponse resResponse) {
        this.resResponse = resResponse;
    }

    
}
