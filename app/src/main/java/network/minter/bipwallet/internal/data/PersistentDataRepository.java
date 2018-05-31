package network.minter.bipwallet.internal.data;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.annimon.stream.Stream;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import network.minter.bipwallet.internal.Wallet;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.common.Lazy;
import network.minter.mintercore.internal.common.LazyMem;
import timber.log.Timber;

/**
 * Atlas_Android. 2017
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class PersistentDataRepository<Service, Result> extends CacheDataRepository<Service, Result> {

    private Lazy<List<RestoreListener<Result>>> mRestoreListeners = LazyMem.memoize(ArrayList::new);
    private Lazy<List<SaveListener<Result>>> mSaveListeners = LazyMem.memoize(ArrayList::new);

    public PersistentDataRepository(ApiService.Builder api, SaveListener<Result> saveListener,
                                    RestoreListener<Result> restoreListener) {
        super(api);
        addSaveListener(saveListener);
        addRestoreListener(restoreListener);
        if (enableAutoRestore()) {
            restore();
        }
    }

    public PersistentDataRepository(ApiService.Builder api, SaveListener<Result> saveListener) {
        super(api);
        addSaveListener(saveListener);
        autoRestore();
    }

    public PersistentDataRepository(ApiService.Builder api,
                                    RestoreListener<Result> restoreListener) {
        super(api);
        addRestoreListener(restoreListener);
        autoRestore();
    }

    public PersistentDataRepository(ApiService.Builder api) {
        super(api);
        autoRestore();
    }

    @CallSuper
    @Override
    public void onAfterUpdate(Result result) {
        super.onAfterUpdate(result);
        log().d("OnAfterUpdate, enable auto save %b", enableAutoSave());
        autoSave();
    }

    public void delete() {
        if (Hawk.contains(key())) {
            log().d("Deleting data from local storage", getClass().getSimpleName());
            Hawk.delete(key());
            clear();
        }
    }

    public void save() {
        log().d("Saving data to local storage");
        onBeforeSave();
        Hawk.put(key(), new PersistentMeta<>(getData(), mExpiredAt));
        onAfterSave(getData());
    }

    public void restore() {
        if (Hawk.contains(key())) {
            log().d("Restoring data from local storage");
            invalidateTime();
            onBeforeRestore();
            PersistentMeta<Service, Result> metaRestore = Hawk.get(key(), null);
            metaRestore.restore(this);
            onAfterRestore(getData());
        } else if (updateOnRestoreIfNotExists()) {
            update(true);
        }
    }

    public void addSaveListener(SaveListener listener) {
        mSaveListeners.get().add(listener);
    }

    public void removeSaveListener(SaveListener listener) {
        mSaveListeners.get().remove(listener);
    }

    public void clearSaveListeners() {
        mSaveListeners.get().clear();
    }

    public void addRestoreListener(RestoreListener listener) {
        mRestoreListeners.get().add(listener);
    }

    public void removeRestoreListener(RestoreListener listener) {
        mRestoreListeners.get().remove(listener);
    }

    public void clearRestoreListeners() {
        mRestoreListeners.get().clear();
    }

    protected boolean enableAutoSave() {
        return false;
    }

    protected boolean enableAutoRestore() {
        return false;
    }

    protected boolean updateOnRestoreIfNotExists() {
        return false;
    }

    protected void onBeforeSave() {
        Stream.of(mSaveListeners.get()).forEach(SaveListener::onBeforeSave);
    }

    protected void onAfterSave(Result result) {
        Stream.of(mSaveListeners.get()).forEach(item -> item.onAfterSave(result));
    }

    protected void onAfterRestore(Result result) {
        Stream.of(mRestoreListeners.get()).forEach(item -> item.onAfterRestore(result));
    }

    protected void onBeforeRestore() {
        Stream.of(mRestoreListeners.get()).forEach(RestoreListener::onBeforeRestore);
    }

    @NonNull
    protected String key() {
        return String.format(Wallet.LC_EN, "data_persistent_repo_%s", getClass().getName());
    }

    private void autoSave() {
        if (!enableAutoSave()) {
            return;
        }

        save();
    }

    private void autoRestore() {
        if (!enableAutoRestore()) return;
        restore();
    }

    private Timber.Tree log() {
        return Timber.tag(getClass().getSimpleName());
    }

    public interface RestoreListener<Result> {
        void onBeforeRestore();
        void onAfterRestore(Result result);
    }

    public interface SaveListener<Result> {
        void onBeforeSave();
        void onAfterSave(Result result);
    }

    static class PersistentMeta<Service, Result> {
        Result data;
        long expiredAtTime = 0;

        PersistentMeta(Result data, Date expiredAt) {
            this.data = data;
            if (expiredAt != null) {
                this.expiredAtTime = expiredAt.getTime();
            }
        }

        void restore(PersistentDataRepository<Service, Result> repository) {
            repository.mExpiredAt = new Date();
            repository.mExpiredAt.setTime(expiredAtTime);
            repository.setDataInternal(data);
        }
    }
}
