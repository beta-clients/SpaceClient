package net.minecraft.src;

public abstract class NetHandler {
	public abstract boolean isServerHandler();

	public void handleMapChunk(Packet51MapChunk packet51mapchunk) {
	}

	public void registerPacket(Packet packet) {
	}

	public void handleErrorMessage(String s, Object[] aobj) {
	}

	public void handleKickDisconnect(Packet255KickDisconnect packet255kickdisconnect) {
		this.registerPacket(packet255kickdisconnect);
	}

	public void handleLogin(Packet1Login packet1login) {
		this.registerPacket(packet1login);
	}

	public void handleFlying(Packet10Flying packet10flying) {
		this.registerPacket(packet10flying);
	}

	public void handleMultiBlockChange(Packet52MultiBlockChange packet52multiblockchange) {
		this.registerPacket(packet52multiblockchange);
	}

	public void handleBlockDig(Packet14BlockDig packet14blockdig) {
		this.registerPacket(packet14blockdig);
	}

	public void handleBlockChange(Packet53BlockChange packet53blockchange) {
		this.registerPacket(packet53blockchange);
	}

	public void handlePreChunk(Packet50PreChunk packet50prechunk) {
		this.registerPacket(packet50prechunk);
	}

	public void handleNamedEntitySpawn(Packet20NamedEntitySpawn packet20namedentityspawn) {
		this.registerPacket(packet20namedentityspawn);
	}

	public void handleEntity(Packet30Entity packet30entity) {
		this.registerPacket(packet30entity);
	}

	public void handleEntityTeleport(Packet34EntityTeleport packet34entityteleport) {
		this.registerPacket(packet34entityteleport);
	}

	public void handlePlace(Packet15Place packet15place) {
		this.registerPacket(packet15place);
	}

	public void handleBlockItemSwitch(Packet16BlockItemSwitch packet16blockitemswitch) {
		this.registerPacket(packet16blockitemswitch);
	}

	public void handleDestroyEntity(Packet29DestroyEntity packet29destroyentity) {
		this.registerPacket(packet29destroyentity);
	}

	public void handlePickupSpawn(Packet21PickupSpawn packet21pickupspawn) {
		this.registerPacket(packet21pickupspawn);
	}

	public void handleCollect(Packet22Collect packet22collect) {
		this.registerPacket(packet22collect);
	}

	public void handleChat(Packet3Chat packet3chat) {
		this.registerPacket(packet3chat);
	}

	public void handleVehicleSpawn(Packet23VehicleSpawn packet23vehiclespawn) {
		this.registerPacket(packet23vehiclespawn);
	}

	public void handleArmAnimation(Packet18Animation packet18animation) {
		this.registerPacket(packet18animation);
	}

	public void func_21147_a(Packet19EntityAction packet19entityaction) {
		this.registerPacket(packet19entityaction);
	}

	public void handleHandshake(Packet2Handshake packet2handshake) {
		this.registerPacket(packet2handshake);
	}

	public void handleMobSpawn(Packet24MobSpawn packet24mobspawn) {
		this.registerPacket(packet24mobspawn);
	}

	public void handleUpdateTime(Packet4UpdateTime packet4updatetime) {
		this.registerPacket(packet4updatetime);
	}

	public void handleSpawnPosition(Packet6SpawnPosition packet6spawnposition) {
		this.registerPacket(packet6spawnposition);
	}

	public void func_6498_a(Packet28EntityVelocity packet28entityvelocity) {
		this.registerPacket(packet28entityvelocity);
	}

	public void func_21148_a(Packet40EntityMetadata packet40entitymetadata) {
		this.registerPacket(packet40entitymetadata);
	}

	public void func_6497_a(Packet39AttachEntity packet39attachentity) {
		this.registerPacket(packet39attachentity);
	}

	public void handleUseEntity(Packet7UseEntity packet7useentity) {
		this.registerPacket(packet7useentity);
	}

	public void func_9447_a(Packet38EntityStatus packet38entitystatus) {
		this.registerPacket(packet38entitystatus);
	}

	public void handleHealth(Packet8UpdateHealth packet8updatehealth) {
		this.registerPacket(packet8updatehealth);
	}

	public void func_9448_a(Packet9Respawn packet9respawn) {
		this.registerPacket(packet9respawn);
	}

	public void func_12245_a(Packet60Explosion packet60explosion) {
		this.registerPacket(packet60explosion);
	}

	public void func_20087_a(Packet100OpenWindow packet100openwindow) {
		this.registerPacket(packet100openwindow);
	}

	public void func_20092_a(Packet101CloseWindow packet101closewindow) {
		this.registerPacket(packet101closewindow);
	}

	public void func_20091_a(Packet102WindowClick packet102windowclick) {
		this.registerPacket(packet102windowclick);
	}

	public void func_20088_a(Packet103SetSlot packet103setslot) {
		this.registerPacket(packet103setslot);
	}

	public void func_20094_a(Packet104WindowItems packet104windowitems) {
		this.registerPacket(packet104windowitems);
	}

	public void handleSignUpdate(Packet130UpdateSign packet130updatesign) {
		this.registerPacket(packet130updatesign);
	}

	public void func_20090_a(Packet105UpdateProgressbar packet105updateprogressbar) {
		this.registerPacket(packet105updateprogressbar);
	}

	public void handlePlayerInventory(Packet5PlayerInventory packet5playerinventory) {
		this.registerPacket(packet5playerinventory);
	}

	public void func_20089_a(Packet106Transaction packet106transaction) {
		this.registerPacket(packet106transaction);
	}

	public void func_21146_a(Packet25EntityPainting packet25entitypainting) {
		this.registerPacket(packet25entitypainting);
	}

	public void handleNotePlay(Packet54PlayNoteBlock packet54playnoteblock) {
		this.registerPacket(packet54playnoteblock);
	}

	public void func_27245_a(Packet200Statistic packet200statistic) {
		this.registerPacket(packet200statistic);
	}

	public void func_22186_a(Packet17Sleep packet17sleep) {
		this.registerPacket(packet17sleep);
	}

	public void func_22185_a(Packet27Position packet27position) {
		this.registerPacket(packet27position);
	}

	public void func_25118_a(Packet70Bed packet70bed) {
		this.registerPacket(packet70bed);
	}

	public void handleWeather(Packet71Weather packet71weather) {
		this.registerPacket(packet71weather);
	}

	public void func_28116_a(Packet131MapData packet131mapdata) {
		this.registerPacket(packet131mapdata);
	}

	public void func_28115_a(Packet61DoorChange packet61doorchange) {
		this.registerPacket(packet61doorchange);
	}

	public void handleAlphaOverride(Packet packet) {
		this.registerPacket(packet);
	}

	public void handleBetaOverride(Packet packet) {
		this.registerPacket(packet);
	}
}
