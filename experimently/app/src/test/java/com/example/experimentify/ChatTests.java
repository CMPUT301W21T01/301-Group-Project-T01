package com.example.experimentify;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ChatTests {

    @Test
    public void TestChatQuestion(){
        chatQuestion question = new chatQuestion("how","abc","1234","01/01/01 01:01:01","321");
        assertEquals(question.getDescription(),"how");
        assertEquals(question.getUID(),"abc");
        assertEquals(question.getEID(),"1234");
        assertEquals(question.getDate(),"01/01/01 01:01:01");
        assertEquals(question.getQID(),"321");

    }

    @Test
    public void TestChatAnswer(){
        //chatAnswer answer = new chatAnswer("how","abc","1234","987");
        chatAnswer answer = new chatAnswer("how","abc","1234","01/01/01 01:01:01","987");
        assertEquals(answer.getDescription(),"how");
        assertEquals(answer.getUID(),"abc");
        assertEquals(answer.getEID(),"1234");
        assertEquals(answer.getQID(),"987");
        assertEquals(answer.getDate(),"01/01/01 01:01:01");



    }


}
