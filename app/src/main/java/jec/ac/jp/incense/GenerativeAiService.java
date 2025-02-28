package jec.ac.jp.incense;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

public class GenerativeAiService {

    private GenerativeModelFutures model;

    public GenerativeAiService(String apiKey) {
        GenerativeModel gm = new GenerativeModel("gemini-2.0-flash", apiKey);
        model = GenerativeModelFutures.from(gm);
    }

    public void generateContent(Content content, Subscriber<GenerateContentResponse> subscriber) {
        Publisher<GenerateContentResponse> streamingResponse = model.generateContentStream(content);
        streamingResponse.subscribe(subscriber);
    }
}
