package com.example.experimentify;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatTests {

    @Test
    public void TestChatQuestion(){
        chatQuestion question = new chatQuestion("how","abc","1234");
        assertEquals(question.getDescription(),"how");
        assertEquals(question.getUID(),"abc");
        assertEquals(question.getEID(),"1234");
        chatQuestion question2 = new chatQuestion("how","abc","1234","01/01/01","321");
        assertEquals(question2.getDescription(),"how");
        assertEquals(question2.getUID(),"abc");
        assertEquals(question2.getEID(),"1234");
        assertEquals(question2.getDate(),"01/01/01");
        assertEquals(question2.getQID(),"321");

    }


    @Test
    public void TestChatAnswer(){
        chatAnswer answer = new chatAnswer("how","abc","1234","987");
        assertEquals(answer.getDescription(),"how");
        assertEquals(answer.getUID(),"abc");
        assertEquals(answer.getEID(),"1234");
        assertEquals(answer.getQID(),"987");
        chatAnswer answer2 = new chatAnswer("how","abc","1234","01/01/01","987");
        assertEquals(answer2.getDescription(),"how");
        assertEquals(answer2.getUID(),"abc");
        assertEquals(answer2.getEID(),"1234");
        assertEquals(answer2.getDate(),"01/01/01");
        assertEquals(answer2.getQID(),"987");


    }


}
