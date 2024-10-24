package isima.crousnotifier.zzz.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SmsRequest {
    @NotBlank
    private final String phoneNumber; // destination
    @NotBlank
    private final String message;

    public SmsRequest(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }
}
