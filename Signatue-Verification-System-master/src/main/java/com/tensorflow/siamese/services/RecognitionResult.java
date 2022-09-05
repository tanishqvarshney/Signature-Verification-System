package com.tensorflow.siamese.services;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.tensorflow.siamese.models.User;
import com.tensorflow.siamese.util.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@Accessors(fluent = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class RecognitionResult {

    List<Pair<User, Double>> userList;

    User bestMatch;

    @Override
    public String toString() {
        return "RecognitionResult{" +
                "userList=" + userList +
                ", bestMatch=" + bestMatch +
                '}';
    }

    public List<Pair<User, Double>> getUserList() {
        return userList;
    }

    public void setUserList(List<Pair<User, Double>> userList) {
        this.userList = userList;
    }

    public User getBestMatch() {
        return bestMatch;
    }

    public void setBestMatch(User bestMatch) {
        this.bestMatch = bestMatch;
    }
}
