package org.example.recommendationservice.service;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;

import java.io.InputStream;

public class OpenNLPService {
    private SentenceDetectorME sentenceDetector;
    private TokenizerME tokenizer;
    private POSTaggerME posTagger;
    private ChunkerME chunker;

    public OpenNLPService() throws Exception {
        try (InputStream sentenceModelStream = getClass().getResourceAsStream("/models/en-sent.bin");
             InputStream tokenModelStream = getClass().getResourceAsStream("/models/en-token.bin");
             InputStream posModelStream = getClass().getResourceAsStream("/models/en-pos-maxent.bin");
             InputStream chunkerModelStream = getClass().getResourceAsStream("/models/en-chunker.bin")) {

            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            TokenizerModel tokenModel = new TokenizerModel(tokenModelStream);
            POSModel posModel = new POSModel(posModelStream);
            ChunkerModel chunkerModel = new ChunkerModel(chunkerModelStream);

            sentenceDetector = new SentenceDetectorME(sentenceModel);
            tokenizer = new TokenizerME(tokenModel);
            posTagger = new POSTaggerME(posModel);
            chunker = new ChunkerME(chunkerModel);
        }
    }

    public String[] detectSentences(String text) {
        return sentenceDetector.sentDetect(text);
    }

    public String[] tokenize(String sentence) {
        return tokenizer.tokenize(sentence);
    }

    public String[] tagPOS(String[] tokens) {
        return posTagger.tag(tokens);
    }

    public String[] chunk(String[] tokens, String[] tags) {
        return chunker.chunk(tokens, tags);
    }
}
