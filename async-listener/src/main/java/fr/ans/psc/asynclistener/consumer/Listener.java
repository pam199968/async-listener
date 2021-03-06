/*
 * Copyright A.N.S 2021
 */
package fr.ans.psc.asynclistener.consumer;

import static fr.ans.psc.asynclistener.config.DLQAmqpConfiguration.QUEUE_CONTACT_MESSAGES;
import static fr.ans.psc.asynclistener.config.DLQAmqpConfiguration.QUEUE_PS_MESSAGES;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import com.google.gson.Gson;

import fr.ans.in.user.api.UserApi;
import fr.ans.in.user.model.ContactInfos;
import fr.ans.psc.ApiClient;
import fr.ans.psc.api.PsApi;
import fr.ans.psc.api.StructureApi;
import fr.ans.psc.asynclistener.model.ContactInfosWithNationalId;
import fr.ans.psc.asynclistener.model.PsAndStructure;
import fr.ans.psc.model.Ps;
import lombok.extern.slf4j.Slf4j;
/**
 * The Class Listener.
 */

@Configuration
@Slf4j
public class Listener {
	
	private final RabbitTemplate rabbitTemplate;

	private final ApiClient client;
	
	private final fr.ans.in.user.ApiClient inClient;
	
	private PsApi psapi;
	
	private StructureApi structureapi;
	
	private UserApi userApi;

	/** The json. */
	Gson json = new Gson();

	/**
	 * Instantiates a new receiver.
	 *
	 * @param client the client
	 * @param inClient the in client
	 * @param rabbitTemplate the rabbit template
	 */
	public Listener(ApiClient client, fr.ans.in.user.ApiClient inClient, RabbitTemplate rabbitTemplate) {
		super();
		this.rabbitTemplate = rabbitTemplate;
		this.client = client;
		this.inClient = inClient;
		init();
	}

	private void init() {
		psapi = new PsApi(client);
		structureapi = new StructureApi(client);
		userApi = new UserApi(inClient);
	}

    /**
     * Dlq amqp container.
     *
     * @return the DLQ contact info amqp container
     */
    @Bean
    public DLQContactInfoAmqpContainer dlqAmqpContainer() {
        return new DLQContactInfoAmqpContainer(rabbitTemplate);
    }
    
    /**
     * process message (Update the PS or create it if not exists). Update structure
     * as well.
     *
     * @param message the message
     * @throws PscUpdateException the psc update exception
     */
    @RabbitListener(queues = QUEUE_PS_MESSAGES)
	public void receivePsMessage(Message message) throws PscUpdateException {
		String messageBody = new String(message.getBody());
		PsAndStructure wrapper = json.fromJson(messageBody, PsAndStructure.class);
		try {
			psapi.createNewPs(wrapper.getPs());
			if (null != structureapi.getStructureById(wrapper.getStructure().getStructureTechnicalId())) {
				structureapi.updateStructure(wrapper.getStructure());
			} else {
				structureapi.createNewStructure(wrapper.getStructure());
			}
		}catch (RestClientException e) {
			log.error("PS {} not updated in DB.", wrapper.getPs().getNationalId());
			log.error("Error : ", e);
		}
	}

    
	/**
	 * process message : Update mail and phone number in database ans push modification to IN Api.
	 *
	 * @param message the message
	 * @throws PscUpdateException the psc update exception
	 */
    @RabbitListener(queues = QUEUE_CONTACT_MESSAGES)
	public void receiveContactMessage(Message message) throws PscUpdateException {
    	log.info("Receiving message...");
		String messageBody = new String(message.getBody());
		ContactInfosWithNationalId contactInput = json.fromJson(messageBody, ContactInfosWithNationalId.class);
		// Get the PS to update
		Ps ps = psapi.getPsById(contactInput.getNationalId());
		ps.setEmail(contactInput.getEmail());
		ps.setPhone(contactInput.getPhone());
		// Update PS in DB
		try {
			psapi.updatePs(ps);
			log.info("Contact informations sent to API : {}", messageBody);
		}catch (RestClientResponseException e) {
			log.error("Contact infos of PS {} not updated in DB (Not requeued) return code : {} ; content : {}", ps.getNationalId(), e.getRawStatusCode(), messageBody);
			//Exit because we don't want to desynchronize PSC DB and IN data
			return;
		} catch (Exception e) {
			log.error("PS {} not updated in DB (It is requeued).", ps.getNationalId(), e);
			// Throw exception to requeue message
				throw new PscUpdateException(e);
		}
		
		try {
			ContactInfos contactOutput = new ContactInfos();
			contactOutput.setEmail(contactInput.getEmail());
			contactOutput.setPhone(contactInput.getPhone());
			userApi.putUsersContactInfos(contactInput.getNationalId(), contactOutput);
			log.info("Contact informations sent to IN : {}", messageBody);
		}catch (RestClientResponseException e) {
			log.error("PS {} not updated at IN : return code {}.", ps.getNationalId(), e.getRawStatusCode());
		} catch (RestClientException e) {
			log.error("PS {} not updated at IN (It is requeued).", ps.getNationalId(), e);
			// Throw exception to requeue message
				throw new PscUpdateException(e);
		}
	}
	
}
