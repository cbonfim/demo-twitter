package br.com.amorim;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class DemoTwitterApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoTwitterApplication.class, args);
	}
        
}
/**
 * 
 * @author cbonfim
 */
@RestController
class TwitterRestController{
    
  
    @Autowired
    TwitterComponent component;

    @RequestMapping(path = "/api/twitter",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
    public List<Tweet> execute(){
        List<Tweet> result = component.getTweets("Bradesco", 20);
        Integer i = component.wordCount(result, "Bradesco");
        System.out.println(i);
        return result;
    }
    
    @RequestMapping(path = "/api/twitter",method=RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody TwitterResponse getWordCount(@RequestBody TwitterRequest twitterRequest){
        List<Tweet> result = component.getTweets(twitterRequest.getHash(), twitterRequest.getQtde());
        Integer i = component.wordCount(result, twitterRequest.getHash());
        return new TwitterResponse(i);
    }
    
    
}

class TwitterResponse implements Serializable{
    private Integer qtd;
    
    public TwitterResponse() {
    }

    public TwitterResponse(Integer qtd) {
        this.qtd = qtd;
    }
    
    

    public Integer getQtd() {
        return qtd;
    }

    public void setQtd(Integer qtd) {
        this.qtd = qtd;
    }
    
    
}

class TwitterRequest implements Serializable{
    
    
    private String hash;
    private Integer qtde;

    public TwitterRequest() {
    }
    
    public TwitterRequest(String hash, Integer qtde) {
        this.hash = hash;
        this.qtde = qtde;
    }
    
    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Integer getQtde() {
        return qtde;
    }

    public void setQtde(Integer qtde) {
        this.qtde = qtde;
    }
    
    
}

@Component
class TwitterComponent{
    
    @Autowired
    public Twitter twitter;
    
    public List<Tweet> getTweets(String value,Integer i){
        List<Tweet> tweets = twitter.searchOperations().search(value, i).getTweets();
        return tweets;
    }

    public Boolean searchWord(String text,String word){
        return text.matches(".*\\b"+word+"\\b.*");
    }
    
    public Integer wordCount(List<Tweet> tweets,String word){
        int count = 0;
        for (Tweet tweet : tweets) {
            if(searchWord(tweet.getText(), word)){
                count++;
            }
        }
        return count;
    }
    
}
