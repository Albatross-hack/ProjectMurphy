package furhatos.app.mrsmurphy.flow.main

import com.theokanning.openai.OpenAiService
import com.theokanning.openai.completion.CompletionRequest
import furhatos.app.mrsmurphy.flow.Parent
import furhatos.flow.kotlin.*
import furhatos.gestures.Gesture
import furhatos.gestures.Gestures
import java.io.File

val serviceKey = "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"


/*fun getOpenDomainResponse(): String {
    var response = ""
    val service = OpenAiService(serviceKey)
    val history = Furhat.dialogHistory.all.takeLast(10).mapNotNull {
        when (it) {
            is DialogHistory.ResponseItem -> {
                "Human: ${it.response.text}"
            }
            is DialogHistory.UtteranceItem -> {
                "Murphy: ${it.toText()}"
            }
            else -> null
        }
    }.joinToString(separator = "\n")
    val prompt=history + "\n Use the above conversation to create an informal response that can be used by a social receptionist to say it as a response to a user without greeting them or wishing within 50 words."
    var lengthofprompt=prompt.length
    println(prompt)
// Read more about these settings: https://beta.openai.com/docs/introduction
    var temperature = 0.9 // Higher values means the model will take more risks. Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
    var maxTokens =  60// Length of output generated. 1 token is on average ~4 characters or 0.75 words for English text
    var topP = 1.0 // 1.0 is default. An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
    var frequencyPenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
    var presencePenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
    val completionRequest = CompletionRequest.builder()
        .temperature(temperature)
        .topP(topP)
        .frequencyPenalty(frequencyPenalty)
        .presencePenalty(presencePenalty)
        .maxTokens(maxTokens)
        .prompt(prompt)
        .echo(true)
        .build();
    try {
        println(history)
        val completion = service.createCompletion("text-davinci-003", completionRequest).getChoices().first().text
        response = completion.trim()
    } catch (e: Exception) {
        println("Problem with connection to OpenAI")
    }
    return response.drop(lengthofprompt)
} */
fun getNLGResponseFromGPT(input: String): String {

    val service = OpenAiService(serviceKey)
    val history = Furhat.dialogHistory.all.takeLast(10).mapNotNull {
        when (it) {
            is DialogHistory.ResponseItem -> {
                "Human: ${it.response.text}"
            }
            is DialogHistory.UtteranceItem -> {
                "Murphy: ${it.toText()}"
            }
            else -> null
        }
    }.joinToString(separator = "\n")
    var conversationInput = "a receptionist called murphy at a place called the national robotarium is talking to a guy generate a casual conversation responce.Information given below maybe used  \n"+input + "\n  The conversation history with human and Murphy is also provided below\n"+history+"\nUse the above information to create an informal conversation that can be used by a social receptionist to say it as a response to a user without greeting them or wishing.Dont give \"Murphy:\" only the dialogue.Summarise the responce to be small and give limited information"
    var response = ""
    val lengthofprompt=conversationInput.length
    // Read more about these settings: https://beta.openai.com/docs/introduction
    var temperature = 0.9 // Higher values means the model will take more risks. Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
    var maxTokens =  100// Length of output generated. 1 token is on average ~4 characters or 0.75 words for English text
    var topP = 1.0 // 1.0 is default. An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
    var frequencyPenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
    var presencePenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
    val completionRequest = CompletionRequest.builder()
        .temperature(temperature)
        .topP(topP)
        .frequencyPenalty(frequencyPenalty)
        .presencePenalty(presencePenalty)
        .maxTokens(maxTokens)
        .prompt(conversationInput)
        .echo(true)
        .build();

    try {
        val completion = service.createCompletion("text-davinci-003", completionRequest).getChoices().first().text
        response = completion.trim()

    } catch (e: Exception) {
        println("Problem with connection to OpenAI" + e.message)
    }
    println(response)
    println("-------------------------------------------------")
    println(response.drop(lengthofprompt))
    return response.drop(lengthofprompt)
}

val GPT: State = state(Parent){
    onEntry{
        var gestureMap : HashMap<String, Gesture>
                = HashMap<String, Gesture> ()
        gestureMap.put("happy", Gestures.BigSmile)
        var GestureContained: ArrayList<Gesture> = ArrayList<Gesture>()
        GestureContained.add(Gestures.BigSmile)
        //gpt3 implmentation
        val dialo: String
        val service = OpenAiService(serviceKey)
        val history = Furhat.dialogHistory.all.takeLast(1).mapNotNull {
            when (it) {
                is DialogHistory.ResponseItem -> {
                    "${it.response.text}"
                }
                else -> null
            }
        }.joinToString(separator = "\n")
        val prompt=history
        val lengthofprompt=prompt.length
        // Read more about these settings: https://beta.openai.com/docs/introduction
        var temperature = 0.0 // Higher values means the model will take more risks. Try 0.9 for more creative applications, and 0 (argmax sampling) for ones with a well-defined answer.
        var maxTokens =  40// Length of output generated. 1 token is on average ~4 characters or 0.75 words for English text
        var topP = 1.0 // 1.0 is default. An alternative to sampling with temperature, called nucleus sampling, where the model considers the results of the tokens with top_p probability mass. So 0.1 means only the tokens comprising the top 10% probability mass are considered.
        var frequencyPenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on their existing frequency in the text so far, decreasing the model's likelihood to repeat the same line verbatim.
        var presencePenalty = 0.0 // Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far, increasing the model's likelihood to talk about new topics.
        val completionRequest = CompletionRequest.builder()
            .temperature(temperature)
            .topP(topP)
            .frequencyPenalty(frequencyPenalty)
            .presencePenalty(presencePenalty)
            .maxTokens(maxTokens)
            .prompt(prompt)
            .echo(true)
            .build();

        try {
            println(history)
            val completion = service.createCompletion("text-davinci-002", completionRequest).getChoices().first().text
            val response = completion.trim()
            furhat.say(response.drop(lengthofprompt))
            for (ges in GestureContained){
                furhat.gesture(ges)
            }

        } catch (e: Exception) {
            println("Problem with connection to OpenAI")
        }
        furhat.listen()

    }
    }