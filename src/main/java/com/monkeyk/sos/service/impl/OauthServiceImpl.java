package com.monkeyk.sos.service.impl;

import com.monkeyk.sos.domain.dto.OauthClientDetailsDto;
import com.monkeyk.sos.domain.oauth.OauthClientDetails;
import com.monkeyk.sos.domain.oauth.OauthRepository;
import com.monkeyk.sos.service.OauthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * OAuth 业务处理服务对象, 事务拦截也加在这一层
 *
 * @author Shengzhao Li
 */
@Service("oauthService")
public class OauthServiceImpl implements OauthService {

    Logger logger= LoggerFactory.getLogger(OauthServiceImpl.class);
    @Autowired
    private OauthRepository oauthRepository;

    @Override
    public OauthClientDetails loadOauthClientDetails(String clientId) {
        return oauthRepository.findOauthClientDetails(clientId);
    }

    @Override
    public List<OauthClientDetailsDto> loadAllOauthClientDetailsDtos() {
        List<OauthClientDetails> clientDetailses = oauthRepository.findAllOauthClientDetails();
        return OauthClientDetailsDto.toDtos(clientDetailses);
    }

    @Override
    public void archiveOauthClientDetails(String clientId) {
        oauthRepository.updateOauthClientDetailsArchive(clientId, true);
    }

    @Override
    public OauthClientDetailsDto loadOauthClientDetailsDto(String clientId) {
        final OauthClientDetails oauthClientDetails = oauthRepository.findOauthClientDetails(clientId);
        return oauthClientDetails != null ? new OauthClientDetailsDto(oauthClientDetails) : null;
    }

    @Override
    public void registerClientDetails(OauthClientDetailsDto formDto) {
        OauthClientDetails clientDetails = formDto.createDomain();
        oauthRepository.saveOauthClientDetails(clientDetails);
    }
}
