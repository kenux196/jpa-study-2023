package study.kenux.jpa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import study.kenux.jpa.domain.Member;
import study.kenux.jpa.repository.MemberRepository;
import study.kenux.jpa.repository.TeamRepository;
import study.kenux.jpa.repository.dto.MemberSearchCond;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;


    List<Member> getMemberByCondition(MemberSearchCond cond, Pageable pageable) {
        return new ArrayList<>();
    }

    Page<Member> getMembers(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }
}
