package spring_rest_docs.rest_docs.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring_rest_docs.rest_docs.controller.MemberModificationRequest;
import spring_rest_docs.rest_docs.controller.MemberSignupRequest;
import spring_rest_docs.rest_docs.dto.MemberDTO;
import spring_rest_docs.rest_docs.dto.MemberDTOMapper;
import spring_rest_docs.rest_docs.entity.MemberEntity;
import spring_rest_docs.rest_docs.repository.JpaMemberRpository;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final JpaMemberRpository repository;

    private final MemberDTOMapper mapper;



    @Transactional
    public MemberDTO save(MemberSignupRequest memberInformationRequest) {

        if (Objects.isNull(memberInformationRequest)){
            throw new RuntimeException("사용자 정보가 없습니다.");
        }

        MemberEntity memberEntity = new MemberEntity(memberInformationRequest.getEmail(), memberInformationRequest.getName());

        MemberEntity members = repository.save(memberEntity);

        return mapper.apply(members);

    }


    @Transactional(readOnly = true)
    public MemberDTO findMember(Long memberId) {
        MemberEntity memberEntity = repository.findById(memberId).orElseThrow(
                () -> new RuntimeException("사용자에 대한 정보가 없습니다."));

        return mapper.apply(memberEntity);
    }


    @Transactional
    public MemberDTO modifyMemberDetails(Long memberId, MemberModificationRequest memberModificationRequest) {
        MemberEntity findMember = repository.findById(memberId).orElseThrow(() -> new RuntimeException("사용자의 대한 정보가 없습니다."));


        findMember.modification(memberModificationRequest);

        log.info("찾은 사용자 정보 = {} ", findMember);

        return mapper.apply(findMember);

    }
}
