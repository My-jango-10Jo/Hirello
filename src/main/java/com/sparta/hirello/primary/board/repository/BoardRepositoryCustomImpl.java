//package com.sparta.hirello.primary.board.repository;
//
//import com.querydsl.core.types.dsl.Wildcard;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.sparta.hirello.primary.board.entity.Board;
//import com.sparta.hirello.primary.user.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//import static com.sparta.hirello.secondary.util.RepositoryUtil.getTotal;
//
//@Repository
//@RequiredArgsConstructor
//public class BoardRepositoryCustomImpl implements BoardRepositoryCustom {
//
//    private final JPAQueryFactory queryFactory;
//
//    @Override
//    public Page<Board> findByUser(User user, Pageable pageable) {
//        // 데이터 조회 쿼리
//        List<Board> boards = queryFactory
//                .selectFrom(post)
//                .join(like).fetchJoin()
//                .on(like.contentId.eq(post.id))
//                .where(
//                        like.user.id.eq(user.getId())
//                )
//                .orderBy(like.likedAt.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        // 전체 카운트 쿼리
//        Long totalCount = queryFactory
//                .select(Wildcard.count) // count(*)
//                .from(post)
//                .join(like).fetchJoin()
//                .on(like.contentId.eq(post.id))
//                .where(
//                        like.user.id.eq(user.getId())
//                )
//                .fetchOne();
//
//        return new PageImpl<>(boards, pageable, getTotal(totalCount));
//    }
//
//}
