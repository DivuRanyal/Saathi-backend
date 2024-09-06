package model.dto;

import java.util.List;

public class PatronCreationResponse {
    private List<PatronDTO> createdPatrons;
    private List<String> conflictMessages;

    // Constructor, getters, and setters
    public PatronCreationResponse(List<PatronDTO> createdPatrons, List<String> conflictMessages) {
        this.createdPatrons = createdPatrons;
        this.conflictMessages = conflictMessages;
    }

    public List<PatronDTO> getCreatedPatrons() {
        return createdPatrons;
    }

    public void setCreatedPatrons(List<PatronDTO> createdPatrons) {
        this.createdPatrons = createdPatrons;
    }

    public List<String> getConflictMessages() {
        return conflictMessages;
    }

    public void setConflictMessages(List<String> conflictMessages) {
        this.conflictMessages = conflictMessages;
    }
}
