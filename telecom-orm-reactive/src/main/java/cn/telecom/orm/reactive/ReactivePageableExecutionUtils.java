package cn.telecom.orm.reactive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ReactivePageableExecutionUtils {
    static <T> Mono<Page<T>> getPage(List<T> content, Pageable pageable, Mono<Long> totalSupplier) {

        Assert.notNull(content, "Content must not be null");
        Assert.notNull(pageable, "Pageable must not be null");
        Assert.notNull(totalSupplier, "TotalSupplier must not be null");

        if (pageable.isUnpaged() || pageable.getOffset() == 0) {

            if (pageable.isUnpaged() || pageable.getPageSize() > content.size()) {
                return Mono.just(new PageImpl<>(content, pageable, content.size()));
            }

            return totalSupplier.map(total -> new PageImpl<>(content, pageable, total));
        }

        if (content.size() != 0 && pageable.getPageSize() > content.size()) {
            return Mono.just(new PageImpl<>(content, pageable, pageable.getOffset() + content.size()));
        }

        return totalSupplier.map(total -> new PageImpl<>(content, pageable, total));
    }
}
