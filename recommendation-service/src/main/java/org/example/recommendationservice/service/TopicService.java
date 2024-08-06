package org.example.recommendationservice.service;

import org.example.recommendationservice.model.Topic;
import org.example.recommendationservice.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;

    public Topic saveTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    public Topic getTopicById(Long id){
        return topicRepository.findById(id).orElse(null);
    }

    public List<Topic> getTopicsByUserId(String userId) {
        return topicRepository.findByUserId(userId);
    }

    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public void deleteTopic(Long id) {
        topicRepository.deleteById(id);
    }
}
