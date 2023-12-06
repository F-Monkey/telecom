package cn.telecom.orm.reactive;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
@NoRepositoryBean
public interface IReactiveRepository<T, ID> extends ReactiveCrudRepository<T, ID>, ReactivePagingAndSortingRepository<T, ID> {
}
