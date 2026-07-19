package net.shishen575.create_upgrades.api;

/**
 * スタックアップグレードを受け付ける機械のマーカーインターフェース。
 * 釜(Basin)・デポ(Depot) の BlockEntity に Mixin で実装される。
 * 他のアドオンの機械に対応させる場合はこのインターフェースを実装する。
 */
public interface IStackUpgradeable {
    // マーカーのみ。スタック倍率は IModuleHolder#getStackMultiplier() で取得する。
}
