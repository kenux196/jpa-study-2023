package study.kenux.jpa.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.kenux.jpa.domain.Member;

import java.util.List;

import static study.kenux.jpa.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepository {

    private final JPAQueryFactory queryFactory;

    public List<Member> findAll() {
        return queryFactory.select(member)
                .from(member)
                .fetch();
    }
}
