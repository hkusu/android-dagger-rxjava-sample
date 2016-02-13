package io.github.hkusu.daggerapp.di;

import javax.inject.Singleton;

import dagger.Component;
import io.github.hkusu.daggerapp.MainActivity;
import io.github.hkusu.daggerapp.adapter.TodoListAdapter;
import io.github.hkusu.daggerapp.model.repository.TodoRepository;
import io.github.hkusu.daggerapp.service.RealmService;
import io.github.hkusu.daggerapp.viewcontroller.UserEventViewController;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
    void inject(MainActivity activity);
    void inject(UserEventViewController viewController);
    void inject(TodoRepository repository);
    void inject(RealmService service);
    void inject(TodoListAdapter.ViewHolder viewHolder);
}